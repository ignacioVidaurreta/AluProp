import { Property } from './property';

export class Service {
  id: number;
  name: string;
  translatedText?: string;
  properties: Property[];
}