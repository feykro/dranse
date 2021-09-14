import { LivreService } from 'app/entities/livre/service/livre.service';
import { Component, Inject, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.scss'],
})
export class BookCardComponent implements OnInit {
  /*
    public titre!: string;
    public auteur!: string;
    public imageURL!: string;
    */
  public titre = 'Book title';
  public auteur = 'Book Author';
  public imageURL = 'https://images-na.ssl-images-amazon.com/images/I/71GByhfLfgL.jpg';

  constructor(@Inject(Number) public bookID: number, private bookService: LivreService) {}

  ngOnInit(): void {
    //this.fetchBook(); //hook this up to the BE api
    if (this.titre === 'hello') {
      this.titre = 'Alternative title';
    }
  }

  fetchBook(): void {
    //todo: se connecter à l'api pour récupérer titre, auteur et image
    //this.bookService.find(this.bookID).subscribe()
    this.titre = 'Le temps des tempêtes';
    this.auteur = 'Nicolas Sarkozy';
    this.imageURL = 'https://images-na.ssl-images-amazon.com/images/I/71GByhfLfgL.jpg';
  }
}
