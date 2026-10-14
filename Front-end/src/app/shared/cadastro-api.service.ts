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

type Recurso = 'atores' | 'diretores' | 'classes';

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
