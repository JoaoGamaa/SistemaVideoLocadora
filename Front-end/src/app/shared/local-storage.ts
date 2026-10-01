export function carregarColecao<T>(chave: string): T[] {
  if (typeof localStorage === 'undefined') {
    return [];
  }

  try {
    const dados = localStorage.getItem(chave);
    return dados ? (JSON.parse(dados) as T[]) : [];
  } catch {
    return [];
  }
}

export function salvarColecao<T>(chave: string, itens: T[]): void {
  if (typeof localStorage === 'undefined') {
    return;
  }

  localStorage.setItem(chave, JSON.stringify(itens));
}

export function gerarProximoId(itens: { id: number }[]): number {
  return itens.length === 0 ? 1 : Math.max(...itens.map((item) => item.id)) + 1;
}
