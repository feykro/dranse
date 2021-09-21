import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-confirmationachat',
  templateUrl: './confirmationachat.component.html',
  styleUrls: ['./confirmationachat.component.scss'],
})
export class ConfirmationachatComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {
    console.log('Confirmation');
  }

  retourMenu(): void {
    this.router.navigate(['']);
  }
}
