package com.example.back_end.model.service;

import com.example.back_end.model.domain.Classe;
import com.example.back_end.model.domain.Diretor;
import com.example.back_end.model.domain.Titulo;
import com.example.back_end.model.repository.ClasseRepository;
import com.example.back_end.model.repository.DiretorRepository;
import com.example.back_end.model.repository.ItemRepository;
import com.example.back_end.model.repository.TituloRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class TituloService {

    private static final int TAMANHO_MAXIMO_NOME = 120;
    private static final int TAMANHO_MAXIMO_GENERO = 60;
    private static final int PRIMEIRO_ANO_DO_CINEMA = 1888;

    private final TituloRepository tituloRepository;
    private final DiretorRepository diretorRepository;
    private final ClasseRepository classeRepository;
    private final ItemRepository itemRepository;

    public TituloService(
            TituloRepository tituloRepository,
            DiretorRepository diretorRepository,
            ClasseRepository classeRepository,
            ItemRepository itemRepository
    ) {
        this.tituloRepository = tituloRepository;
        this.diretorRepository = diretorRepository;
        this.classeRepository = classeRepository;
        this.itemRepository = itemRepository;
    }

    public Titulo cadastrar(Titulo dadosTitulo) {
        DadosValidados dados = validar(dadosTitulo);
        validarDuplicidade(dados.nome(), dados.anoLancamento(), null);

        return tituloRepository.save(new Titulo(
                dados.nome(),
                dados.anoLancamento(),
                dados.genero(),
                dados.diretor(),
                dados.classe()
        ));
    }

    @Transactional(readOnly = true)
    public List<Titulo> listarTodos() {
        return tituloRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional(readOnly = true)
    public Titulo buscarPorId(Long id) {
        return tituloRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Título não encontrado"));
    }

    public Titulo atualizar(Long id, Titulo dadosTitulo) {
        Titulo tituloExistente = buscarPorId(id);
        DadosValidados dados = validar(dadosTitulo);
        validarDuplicidade(dados.nome(), dados.anoLancamento(), id);

        tituloExistente.setNome(dados.nome());
        tituloExistente.setAnoLancamento(dados.anoLancamento());
        tituloExistente.setGenero(dados.genero());
        tituloExistente.setDiretor(dados.diretor());
        tituloExistente.setClasse(dados.classe());
        return tituloRepository.save(tituloExistente);
    }

    public void excluir(Long id) {
        Titulo tituloExistente = buscarPorId(id);
        if (itemRepository.existsByTituloId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir um título que possui itens cadastrados"
            );
        }
        tituloRepository.delete(tituloExistente);
    }

    private DadosValidados validar(Titulo dadosTitulo) {
        if (dadosTitulo == null) {
            throw new IllegalArgumentException("O título deve ser informado");
        }

        String nome = normalizarTexto(dadosTitulo.getNome(), "O nome do título deve ser informado");
        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            throw new IllegalArgumentException("O nome do título deve ter no máximo 120 caracteres");
        }

        Integer anoLancamento = dadosTitulo.getAnoLancamento();
        int proximoAno = Year.now().getValue() + 1;
        if (anoLancamento == null
                || anoLancamento < PRIMEIRO_ANO_DO_CINEMA
                || anoLancamento > proximoAno) {
            throw new IllegalArgumentException("Informe um ano de lançamento válido");
        }

        String genero = normalizarTexto(dadosTitulo.getGenero(), "O gênero deve ser informado");
        if (genero.length() > TAMANHO_MAXIMO_GENERO) {
            throw new IllegalArgumentException("O gênero deve ter no máximo 60 caracteres");
        }

        Long diretorId = extrairDiretorId(dadosTitulo);
        Diretor diretor = diretorRepository.findById(diretorId)
                .orElseThrow(() -> new NoSuchElementException("Diretor não encontrado"));

        Long classeId = extrairClasseId(dadosTitulo);
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new NoSuchElementException("Classe não encontrada"));

        return new DadosValidados(nome, anoLancamento, genero, diretor, classe);
    }

    private String normalizarTexto(String texto, String mensagem) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return texto.trim();
    }

    private Long extrairDiretorId(Titulo titulo) {
        if (titulo.getDiretor() == null || titulo.getDiretor().getId() == null) {
            throw new IllegalArgumentException("O diretor deve ser informado");
        }
        return titulo.getDiretor().getId();
    }

    private Long extrairClasseId(Titulo titulo) {
        if (titulo.getClasse() == null || titulo.getClasse().getId() == null) {
            throw new IllegalArgumentException("A classe deve ser informada");
        }
        return titulo.getClasse().getId();
    }

    private void validarDuplicidade(String nome, Integer anoLancamento, Long idIgnorado) {
        boolean duplicado = idIgnorado == null
                ? tituloRepository.existsByNomeIgnoreCaseAndAnoLancamento(nome, anoLancamento)
                : tituloRepository.existsByNomeIgnoreCaseAndAnoLancamentoAndIdNot(
                        nome,
                        anoLancamento,
                        idIgnorado
                );

        if (duplicado) {
            throw new IllegalArgumentException(
                    "Já existe um título cadastrado com esse nome e ano de lançamento"
            );
        }
    }

    private record DadosValidados(
            String nome,
            Integer anoLancamento,
            String genero,
            Diretor diretor,
            Classe classe
    ) {
    }
}
