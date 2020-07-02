import { Neighborhood } from './neighborhood';
import { Image } from './image';
import { Rule } from './rule';
import { Service } from './service';
import {User} from "./user";

export class Property {
  id: number;
  caption: string;
  description: string;
  propertyType: PropertyType;
  neighbourhood: Neighborhood;
  address: string;
  privacyLevel: PrivacyLevel;
  capacity: number;
  price: number;
  availability: Availability;
  image: any;// TODO: has to be Image[] and change components that access this
  images: Image[];
  owner: User;
  rules: Rule[];
  services: Service[];
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

export type Availability  = 'AVAILABLE' | 'RENTED';
export const Availability = {
  Available: 'AVAILABLE' as Availability,
  Rented: 'RENTED' as Availability,
};
