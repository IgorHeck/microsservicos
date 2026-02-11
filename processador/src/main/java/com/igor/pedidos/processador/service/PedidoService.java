package com.igor.pedidos.processador.service;

import com.igor.pedidos.processador.entity.ItemPedido;
import com.igor.pedidos.processador.entity.Pedido;
import com.igor.pedidos.processador.repository.ItemPedidoRepository;
import com.igor.pedidos.processador.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final Logger logger = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final ItemPedidoService itemPedidoService;
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoService produtoService, ItemPedidoService itemPedidoService, ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
        this.itemPedidoService = itemPedidoService;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public void save(Pedido pedido) {

        produtoService.save(pedido.getItens());

        List<ItemPedido> itemPedidos = itemPedidoService.save(pedido.getItens());

        pedidoRepository.save(pedido);

        itemPedidoService.updatedItemPedido(itemPedidos, pedido);

        logger.info("Pedido salvo: {}", pedido.toString());

    }

}
