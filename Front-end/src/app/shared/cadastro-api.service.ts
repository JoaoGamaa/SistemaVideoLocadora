import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface AtorRegistro {
  id: number;
  nome: string;
}

export interface DiretorRegistro {
  id: number;
  nome: string;
}

export interface ClasseRegistro {
  id: number;
  nome: string;
  valorDiaria: number;
}

export interface TituloRegistro {
  id: number;
  nome: string;
  anoLancamento: number;
  genero: string;
  diretor: DiretorRegistro;
  classe: ClasseRegistro;
}

export type TipoItem = 'DVD' | 'BLU_RAY' | 'VHS';

export interface ItemRegistro {
  id: number;
  numeroSerie: string;
  tipo: TipoItem;
  dataAquisicao: string;
  disponivel: boolean;
  titulo: TituloRegistro;
}

type Recurso = 'atores' | 'diretores' | 'classes' | 'titulos' | 'itens';

@Injectable({ providedIn: 'root' })
export class CadastroApiService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api';

  listarAtores(): Observable<AtorRegistro[]> {
    return this.listar<AtorRegistro>('atores');
  }

  cadastrarAtor(nome: string): Observable<AtorRegistro> {
    return this.cadastrar<AtorRegistro>('atores', { nome });
  }

  atualizarAtor(id: number, nome: string): Observable<AtorRegistro> {
    return this.atualizar<AtorRegistro>('atores', id, { nome });
  }

  excluirAtor(id: number): Observable<void> {
    return this.excluir('atores', id);
  }

  listarDiretores(): Observable<DiretorRegistro[]> {
    return this.listar<DiretorRegistro>('diretores');
  }

  cadastrarDiretor(nome: string): Observable<DiretorRegistro> {
    return this.cadastrar<DiretorRegistro>('diretores', { nome });
  }

  atualizarDiretor(id: number, nome: string): Observable<DiretorRegistro> {
    return this.atualizar<DiretorRegistro>('diretores', id, { nome });
  }

  excluirDiretor(id: number): Observable<void> {
    return this.excluir('diretores', id);
  }

  listarClasses(): Observable<ClasseRegistro[]> {
    return this.listar<ClasseRegistro>('classes');
  }

  cadastrarClasse(nome: string, valorDiaria: number): Observable<ClasseRegistro> {
    return this.cadastrar<ClasseRegistro>('classes', { nome, valorDiaria });
  }

  atualizarClasse(
    id: number,
    nome: string,
    valorDiaria: number,
  ): Observable<ClasseRegistro> {
    return this.atualizar<ClasseRegistro>('classes', id, { nome, valorDiaria });
  }

  excluirClasse(id: number): Observable<void> {
    return this.excluir('classes', id);
  }

  listarTitulos(): Observable<TituloRegistro[]> {
    return this.listar<TituloRegistro>('titulos');
  }

  cadastrarTitulo(
    nome: string,
    anoLancamento: number,
    genero: string,
    diretorId: number,
    classeId: number,
  ): Observable<TituloRegistro> {
    return this.cadastrar<TituloRegistro>('titulos', {
      nome,
      anoLancamento,
      genero,
      diretor: { id: diretorId },
      classe: { id: classeId },
    });
  }

  atualizarTitulo(
    id: number,
    nome: string,
    anoLancamento: number,
    genero: string,
    diretorId: number,
    classeId: number,
  ): Observable<TituloRegistro> {
    return this.atualizar<TituloRegistro>('titulos', id, {
      nome,
      anoLancamento,
      genero,
      diretor: { id: diretorId },
      classe: { id: classeId },
    });
  }

  excluirTitulo(id: number): Observable<void> {
    return this.excluir('titulos', id);
  }

  listarItens(): Observable<ItemRegistro[]> {
    return this.listar<ItemRegistro>('itens');
  }

  cadastrarItem(
    numeroSerie: string,
    tipo: TipoItem,
    dataAquisicao: string,
    disponivel: boolean,
    tituloId: number,
  ): Observable<ItemRegistro> {
    return this.cadastrar<ItemRegistro>('itens', {
      numeroSerie,
      tipo,
      dataAquisicao,
      disponivel,
      titulo: { id: tituloId },
    });
  }

  atualizarItem(
    id: number,
    numeroSerie: string,
    tipo: TipoItem,
    dataAquisicao: string,
    disponivel: boolean,
    tituloId: number,
  ): Observable<ItemRegistro> {
    return this.atualizar<ItemRegistro>('itens', id, {
      numeroSerie,
      tipo,
      dataAquisicao,
      disponivel,
      titulo: { id: tituloId },
    });
  }

  excluirItem(id: number): Observable<void> {
    return this.excluir('itens', id);
  }

  private listar<T>(recurso: Recurso): Observable<T[]> {
    return this.http.get<T[]>(`${this.apiUrl}/${recurso}`);
  }

  private cadastrar<T>(recurso: Recurso, dados: object): Observable<T> {
    return this.http.post<T>(`${this.apiUrl}/${recurso}`, dados);
  }

  private atualizar<T>(recurso: Recurso, id: number, dados: object): Observable<T> {
    return this.http.put<T>(`${this.apiUrl}/${recurso}/${id}`, dados);
  }

  private excluir(recurso: Recurso, id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${recurso}/${id}`);
  }
}

export function mensagemDaApi(erro: unknown, mensagemPadrao: string): string {
  if (erro instanceof HttpErrorResponse) {
    const mensagem = erro.error?.mensagem;
    if (typeof mensagem === 'string' && mensagem.trim()) {
      return mensagem;
    }
  }
  return mensagemPadrao;
}
