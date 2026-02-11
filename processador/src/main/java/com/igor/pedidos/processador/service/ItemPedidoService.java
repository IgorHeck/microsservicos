package com.igor.pedidos.processador.service;

import com.igor.pedidos.processador.entity.ItemPedido;
import com.igor.pedidos.processador.entity.Pedido;
import com.igor.pedidos.processador.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<ItemPedido> save(List<ItemPedido> itens) {
        return itemPedidoRepository.saveAll(itens);
    }

    public void save(ItemPedido item) {
        itemPedidoRepository.save(item);
    }

    public void updatedItemPedido(List<ItemPedido> itemPedidos, Pedido pedido) {

        itemPedidos.forEach(item -> {
            item.setPedido(pedido);
            this.save(item);
        });

    }
}
