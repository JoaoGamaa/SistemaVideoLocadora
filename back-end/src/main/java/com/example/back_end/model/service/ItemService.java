package com.example.back_end.model.service;

import com.example.back_end.model.domain.Item;
import com.example.back_end.model.domain.Titulo;
import com.example.back_end.model.repository.ItemRepository;
import com.example.back_end.model.repository.TituloRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Transactional
public class ItemService {

    private static final int TAMANHO_MAXIMO_NUMERO_SERIE = 50;
    private static final Set<String> TIPOS_VALIDOS = Set.of("DVD", "BLU_RAY", "VHS");

    private final ItemRepository itemRepository;
    private final TituloRepository tituloRepository;

    public ItemService(ItemRepository itemRepository, TituloRepository tituloRepository) {
        this.itemRepository = itemRepository;
        this.tituloRepository = tituloRepository;
    }

    public Item cadastrar(Item dadosItem) {
        DadosValidados dados = validar(dadosItem);
        validarNumeroSerieDuplicado(dados.numeroSerie(), null);

        return itemRepository.save(new Item(
                dados.numeroSerie(),
                dados.tipo(),
                dados.dataAquisicao(),
                dados.disponivel(),
                dados.titulo()
        ));
    }

    @Transactional(readOnly = true)
    public List<Item> listarTodos() {
        return itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional(readOnly = true)
    public Item buscarPorId(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item não encontrado"));
    }

    public Item atualizar(Long id, Item dadosItem) {
        Item itemExistente = buscarPorId(id);
        DadosValidados dados = validar(dadosItem);
        validarNumeroSerieDuplicado(dados.numeroSerie(), id);

        itemExistente.setNumeroSerie(dados.numeroSerie());
        itemExistente.setTipo(dados.tipo());
        itemExistente.setDataAquisicao(dados.dataAquisicao());
        itemExistente.setDisponivel(dados.disponivel());
        itemExistente.setTitulo(dados.titulo());
        return itemRepository.save(itemExistente);
    }

    public void excluir(Long id) {
        Item itemExistente = buscarPorId(id);
        itemRepository.delete(itemExistente);
    }

    private DadosValidados validar(Item dadosItem) {
        if (dadosItem == null) {
            throw new IllegalArgumentException("O item deve ser informado");
        }

        String numeroSerie = dadosItem.getNumeroSerie();
        if (numeroSerie == null || numeroSerie.isBlank()) {
            throw new IllegalArgumentException("O número de série deve ser informado");
        }
        numeroSerie = numeroSerie.trim();
        if (numeroSerie.length() > TAMANHO_MAXIMO_NUMERO_SERIE) {
            throw new IllegalArgumentException(
                    "O número de série deve ter no máximo 50 caracteres"
            );
        }

        String tipo = dadosItem.getTipo();
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("O tipo de mídia deve ser informado");
        }
        tipo = tipo.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        if (!TIPOS_VALIDOS.contains(tipo)) {
            throw new IllegalArgumentException("O tipo de mídia deve ser DVD, BLU_RAY ou VHS");
        }

        LocalDate dataAquisicao = dadosItem.getDataAquisicao();
        if (dataAquisicao == null || dataAquisicao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Informe uma data de aquisição válida");
        }

        if (dadosItem.getTitulo() == null || dadosItem.getTitulo().getId() == null) {
            throw new IllegalArgumentException("O título deve ser informado");
        }
        Titulo titulo = tituloRepository.findById(dadosItem.getTitulo().getId())
                .orElseThrow(() -> new NoSuchElementException("Título não encontrado"));

        return new DadosValidados(
                numeroSerie,
                tipo,
                dataAquisicao,
                dadosItem.isDisponivel(),
                titulo
        );
    }

    private void validarNumeroSerieDuplicado(String numeroSerie, Long idIgnorado) {
        boolean duplicado = idIgnorado == null
                ? itemRepository.existsByNumeroSerieIgnoreCase(numeroSerie)
                : itemRepository.existsByNumeroSerieIgnoreCaseAndIdNot(numeroSerie, idIgnorado);

        if (duplicado) {
            throw new IllegalArgumentException("Já existe um item com esse número de série");
        }
    }

    private record DadosValidados(
            String numeroSerie,
            String tipo,
            LocalDate dataAquisicao,
            boolean disponivel,
            Titulo titulo
    ) {
    }
}
