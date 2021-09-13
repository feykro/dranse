import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ProduitComponent} from "./produit/produit.component";
import {DranseRoutingModule} from "./dranse-routing.module";


@NgModule({
  declarations: [ProduitComponent],
  imports: [
    CommonModule, DranseRoutingModule
  ]
})
export class DranseModule { }
