import { Property } from './property';

export class Rule {
  id: number;
  name: string;
  translatedText?: string;
  properties: Property[]
}