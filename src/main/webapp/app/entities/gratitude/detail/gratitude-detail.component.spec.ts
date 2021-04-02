import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GratitudeDetailComponent } from './gratitude-detail.component';

describe('Component Tests', () => {
  describe('Gratitude Management Detail Component', () => {
    let comp: GratitudeDetailComponent;
    let fixture: ComponentFixture<GratitudeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GratitudeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ gratitude: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GratitudeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GratitudeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load gratitude on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.gratitude).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
