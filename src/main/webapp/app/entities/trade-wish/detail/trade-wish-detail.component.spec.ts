import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TradeWishDetailComponent } from './trade-wish-detail.component';

describe('Component Tests', () => {
  describe('TradeWish Management Detail Component', () => {
    let comp: TradeWishDetailComponent;
    let fixture: ComponentFixture<TradeWishDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TradeWishDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tradeWish: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TradeWishDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TradeWishDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tradeWish on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tradeWish).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
