import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { GratitudeDetailComponent } from './gratitude-detail.component';

describe('Component Tests', () => {
  describe('Gratitude Management Detail Component', () => {
    let comp: GratitudeDetailComponent;
    let fixture: ComponentFixture<GratitudeDetailComponent>;
    let dataUtils: DataUtils;

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
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load gratitude on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.gratitude).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
