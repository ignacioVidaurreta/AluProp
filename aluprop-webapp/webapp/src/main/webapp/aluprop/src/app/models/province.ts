import { City } from './city';
import { Country } from './country';

export class Province{
  id: number;
  country: Country;
  name: string;
  cities: City[];
}