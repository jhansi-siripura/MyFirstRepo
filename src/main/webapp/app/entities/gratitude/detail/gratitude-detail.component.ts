import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGratitude } from '../gratitude.model';

@Component({
  selector: 'jhi-gratitude-detail',
  templateUrl: './gratitude-detail.component.html',
})
export class GratitudeDetailComponent implements OnInit {
  gratitude: IGratitude | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gratitude }) => {
      this.gratitude = gratitude;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
