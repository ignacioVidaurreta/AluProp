import { Province } from './province';
import { City } from './city';

export class Country{
  id: number;
  name: string;
  provinces: Province[];
  cities: City[];
}