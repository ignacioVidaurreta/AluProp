import { Province } from './province';
import { Country } from './country';
import { Neighborhood } from './neighborhood';

export class City{
  id: number;
  name: string;
  province: Province;
  country: Country;
  neighborhoods: Neighborhood[];
}