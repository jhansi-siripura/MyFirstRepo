import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGratitude } from '../gratitude.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { GratitudeService } from '../service/gratitude.service';
import { GratitudeDeleteDialogComponent } from '../delete/gratitude-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { threadId } from 'node:worker_threads';

@Component({
  selector: 'jhi-gratitude-today',
  templateUrl: './gratitude-today.component.html',
})
export class GratitudeTodayComponent implements OnInit {
  ACHIEVED = '/achieved';
  LOVED = '/loved';
  TODAY = '/today';
  YDAY = '/yday';

  gratitudes?: IGratitude[];

  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  heartClass = ['far', 'heart'];

  constructor(
    protected gratitudeService: GratitudeService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.gratitudeService
      .queryBy(this.ACHIEVED, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IGratitude[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  getLoved(): void {
    this.loadPagByField(this.LOVED);
  }

  getAchieved(): void {
    this.loadPagByField(this.ACHIEVED);
  }

  getToday(): void {
    this.loadPagByField(this.TODAY);
  }

  getYday(): void {
    this.loadPagByField(this.YDAY);
  }

  loadPagByField(byField?: string, page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    const theField: string = byField ?? this.ACHIEVED;

    this.gratitudeService
      .queryBy(theField, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IGratitude[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation(this.ACHIEVED);
  }

  trackId(index: number, item: IGratitude): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(gratitude: IGratitude): void {
    const modalRef = this.modalService.open(GratitudeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.gratitude = gratitude;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  heartUpdate(loved: boolean): void {
    //console.log('Loved ' + loved);
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(byField: string): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IGratitude[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/gratitude'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.gratitudes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
