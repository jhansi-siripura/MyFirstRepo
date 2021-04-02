import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WishDetailComponent } from './wish-detail.component';

describe('Component Tests', () => {
  describe('Wish Management Detail Component', () => {
    let comp: WishDetailComponent;
    let fixture: ComponentFixture<WishDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WishDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ wish: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WishDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WishDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load wish on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.wish).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
