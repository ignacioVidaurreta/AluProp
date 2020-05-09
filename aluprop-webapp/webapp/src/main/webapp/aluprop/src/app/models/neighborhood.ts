import { Property } from './property';
import { City } from './city';

export class Neighborhood{
  id: number;
  name: string;
  city: City;
  properties: Property[];
}