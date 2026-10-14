import { Routes } from '@angular/router';
import { Ator } from './pages/ator/ator';
import { Classe } from './pages/classe/classe';
import { Diretor } from './pages/diretor/diretor';
import { Home } from './pages/home/home';
import { Item } from './pages/item/item';
import { Titulo } from './pages/titulo/titulo';

export const routes: Routes = [
  { path: '', component: Home, title: 'Início | Locadora' },
  { path: 'titulos', component: Titulo, title: 'Títulos | Locadora' },
  { path: 'itens', component: Item, title: 'Itens | Locadora' },
  { path: 'atores', component: Ator, title: 'Atores | Locadora' },
  { path: 'classes', component: Classe, title: 'Classes | Locadora' },
  { path: 'diretores', component: Diretor, title: 'Diretores | Locadora' },
  { path: '**', redirectTo: '' },
];
