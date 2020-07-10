export interface PageResponse<T>{
  pageNumber: number;
  pageSize: number;
  totalItems: number;
  responseData: T[];
}