import { Neighborhood } from './neighborhood';

export class Property {
  id: number;
  caption: string;
  description: string;
  propertyType: PropertyType;
  neighborhood: Neighborhood;
  address: string;
  privacyLevel: PrivacyLevel;
  capacity: number;
  price: number;
}

export type PropertyType  = 'HOUSE' | 'APARTMENT' | 'LOFT';
export const PropertyType = {
  House: 'HOUSE' as PropertyType,
  Apartment: 'APARTMENT' as PropertyType,
  Loft: 'LOFT' as PropertyType
};

export type PrivacyLevel  = true | false;
export const PrivacyLevel = {
  Shared: true as PrivacyLevel,
  Individual: false as PrivacyLevel
};