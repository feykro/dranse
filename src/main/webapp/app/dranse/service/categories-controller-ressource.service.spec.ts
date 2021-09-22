import { TestBed } from '@angular/core/testing';

import { CategoriesControllerRessourceService } from './categories-controller-ressource.service';

describe('CategoriesControllerRessourceService', () => {
  let service: CategoriesControllerRessourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoriesControllerRessourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
