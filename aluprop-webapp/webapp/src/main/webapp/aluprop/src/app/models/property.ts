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
  mainImage: Image;// TODO: has to be erased and usages changed to images
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

export type SortOption = 'NEWEST' | 
                          'CAPACITY_DESC' | 
                          'CAPACITY' | 
                          'PRINCE_DESC' | 
                          'PRICE' | 
                          'BUDGET_DESC' | 
                          'BUDGET';
export const SortOption = {
  Newest: 'NEWEST' as SortOption,
  HighestCapacity: 'CAPACITY_DESC' as SortOption,
  LowestCapacity: 'CAPACITY' as SortOption,
  HighestPrice: 'PRICE_DESC' as SortOption,
  LowestPrice: 'PRICE' as SortOption,
  HighestBudget: 'BUDGET_DESC' as SortOption,
  LowestBudget: 'BUDGET' as SortOption,
};              