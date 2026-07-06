package com.martinviscarra.microservices.project.cart_service.service;

import com.martinviscarra.microservices.project.cart_service.dto.cart.CartDetailDto;
import com.martinviscarra.microservices.project.cart_service.dto.cart.CartRequestDto;
import com.martinviscarra.microservices.project.cart_service.dto.cart.CartResponseDto;
import com.martinviscarra.microservices.project.cart_service.dto.item.ItemDetailDto;
import com.martinviscarra.microservices.project.cart_service.dto.item.ItemRequestDto;
import com.martinviscarra.microservices.project.cart_service.dto.item.ItemResponseDto;
import com.martinviscarra.microservices.project.cart_service.dto.product.ProductResponseDto;
import com.martinviscarra.microservices.project.cart_service.exception.BusinessRuleException;
import com.martinviscarra.microservices.project.cart_service.model.Cart;
import com.martinviscarra.microservices.project.cart_service.model.CartItem;
import com.martinviscarra.microservices.project.cart_service.network.ProductClient;
import com.martinviscarra.microservices.project.cart_service.repository.ICartRepository;
import com.martinviscarra.microservices.project.cart_service.utils.CartStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final ICartRepository cartRepository;
    private final ProductClient productClient;


    //-------------------------------------------------Save
    @Transactional
    @Override
    public CartResponseDto save(CartRequestDto request) {

        // Obtenemos la lista de IDs
        List<Long> ids = getListIds(request);

        // Obtenemos la lista de productos para validar su existencia
        getProductsByIds(ids);

        Cart cart = createCart(request);

        return entityCartToDto(cartRepository.save(cart));
    }

    private CartResponseDto entityCartToDto(Cart cart) {
        return CartResponseDto.builder()
                .cartId(cart.getId())
                .status(cart.getStatus().name())
                .items(cart.getItems().stream().map(this::entityItemToDto).toList())
                .build();
    }

    private ItemResponseDto entityItemToDto(CartItem item) {
        return ItemResponseDto.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .build();
    }


    private Cart createCart(CartRequestDto request) {
        Cart cart = Cart.builder()
                .status(CartStatus.OPEN)
                .build();
        cart.setItems(createItems(request.getItems(), cart));

        return cart;
    }

    private List<CartItem> createItems(List<ItemRequestDto> itemsRequest, Cart cart) {

        List<CartItem> cartItems = new ArrayList<>();
        for (ItemRequestDto itemRequest : itemsRequest) {
            CartItem cartItem = CartItem.builder()
                    .quantity(itemRequest.getQuantity())
                    .productId(itemRequest.getProductId())
                    .cart(cart)
                    .build();

            cartItems.add(cartItem);
        }

        return cartItems;
    }


    private List<ProductResponseDto> getProductsByIds(List<Long> ids) {
        List<ProductResponseDto> products = productClient.getProductsByIds(ids);

        if (ids.size() != products.size()) {
            throw new BusinessRuleException("Uno o más productos seleccionados no existen en el catálogo.");
        }
        return products;
    }

    private List<Long> getListIds(CartRequestDto list) {
        return list.getItems().stream()
                .map(ItemRequestDto::getProductId)
                .distinct()
                .toList();
    }

    private List<Long> getListIdsByItems(List<CartItem> items) {
        return items.stream()
                .map(CartItem::getProductId)
                .distinct()
                .toList();
    }

    //-----------------------------ADD ITEM
    @Transactional
    @Override
    public ItemResponseDto addItem(Long cartId, ItemRequestDto request) {

        Cart cart = findCartAndVerifyStatus(cartId, CartStatus.OPEN);
        ProductResponseDto product = getProductById(request.getProductId());

        CartItem cartItem = findItemFromCart(cart, request.getProductId()); // Buscamos al item relacionado al carrito
        cartItem = modifyItemQuantityOrSave(cart, cartItem, request); // Si existe, modificamos su cantidad. Sino lo agregamos a la lista del carrito

        Cart savedCart = cartRepository.save(cart);
        return entityItemToDto(cartItem);
    }

    private Cart findCartAndVerifyStatus(Long id, CartStatus cartStatus) {
        Cart cart = findCart(id);

        if (cart.getStatus() != cartStatus) {
            throw new BusinessRuleException("La accion no puede concretarse porque el estado del carrito es " + cartStatus);
        }

        return cart;
    }

    private Cart findCart(Long id) {
        return cartRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se encontró al carrito de id: " + id)
        );
    }

    private ProductResponseDto getProductById(Long productId) {
        ProductResponseDto p = productClient.getProductById(productId);
        if (!p.isActive()) {
            throw new BusinessRuleException("El producto '" + p.getName() + "' se encuentra inactivo.");
        }
        return p;
    }

    private CartItem modifyItemQuantityOrSave(Cart cart, CartItem cartItem, ItemRequestDto request) {
        if (cartItem != null) {
            //El item ya estaba relacionado al carrito, aumentamos la cantidad
            cartItem.setQuantity(request.getQuantity());
            return cartItem;

        } else {
            //El item no estaba relacionado al carrito, lo agregamos
            CartItem cartItemToSave = CartItem.builder()
                    .cart(cart)
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .build();

            cart.getItems().add(cartItemToSave);
            return cartItemToSave;
        }
    }

    private CartItem findItemFromCart(Cart cart, Long productId) {
        return cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst().orElse(null);
    }


    //-----------------------DELETE ITEM
    @Transactional
    @Override
    public void deleteItem(Long cartId, Long productId) {

        Cart cart = findCartAndVerifyStatus(cartId, CartStatus.OPEN);

        removeItemFromList(cart.getItems(), productId);

        cartRepository.save(cart); // Llamada explícita por legibilidad del código; la sincronización real la maneja el dirty-checking de JPA
    }

    public void removeItemFromList(List<CartItem> items, Long productId) {
        boolean wasRemoved = items.removeIf(item -> item.getProductId().equals(productId));

        if (!wasRemoved) {
            throw new BusinessRuleException("El producto con ID " + productId + " no se encontraba en el carrito.");
        }
    }


    //---------------------------------------------------------GetCartById
    @Transactional(readOnly = true)
    @Override
    public CartDetailDto getCartById(Long cartId) {

        // Buscamos al carrito
        Cart cart = findCart(cartId);

        // Obtenemos la información de los productos relacionados al carrito obtenido
        List<ProductResponseDto> products = productClient.getProductsByIds(getListIdsByItems(cart.getItems()));

        // Creamos los DTOs de respuesta
        return entityCartToDetailDto(cart, products);

    }

    private CartDetailDto entityCartToDetailDto(Cart cart, List<ProductResponseDto> products) {

        List<ItemDetailDto> itemsDetail = entityItemsToDetailsDto(cart.getItems(), products);

        // Calculamos el total sumando únicamente los subtotales ya calculados
        BigDecimal totalPrice = itemsDetail.stream()
                .map(ItemDetailDto::getItemSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartDetailDto.builder()
                .id(cart.getId())
                .status(cart.getStatus().name())
                .totalPrice(totalPrice)
                .items(itemsDetail)
                .build();

    }

    private List<ItemDetailDto> entityItemsToDetailsDto(List<CartItem> items, List<ProductResponseDto> products) {

        // Transformamos la lista a un Mapa [ID -> Producto]
        Map<Long, ProductResponseDto> productMap = getProductMap(products);
        List<ItemDetailDto> itemsDetailsDto = new ArrayList<>();

        for (CartItem item : items) {

            // Obtenemos el producto referenciado por el item
            ProductResponseDto product = productMap.get(item.getProductId());

            // Calculamos el subtotal para este item
            BigDecimal quantityBigDecimal = BigDecimal.valueOf(item.getQuantity());
            BigDecimal price = product.getPrice().multiply(quantityBigDecimal);

            ItemDetailDto itemDetailDto = ItemDetailDto.builder()
                    .itemId(item.getId())
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .nameProduct(product.getName())
                    .brandProduct(product.getBrand())
                    .unitPrice(product.getPrice())
                    .itemSubTotal(price)
                    .build();

            itemsDetailsDto.add(itemDetailDto);
        }

        return itemsDetailsDto;

    }


    private Map<Long, ProductResponseDto> getProductMap(List<ProductResponseDto> products) {
        return products.stream()
                .collect(Collectors.toMap(ProductResponseDto::getId, p -> p));

    }

}
