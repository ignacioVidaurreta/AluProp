import {Property} from "./property";
import {UserProposal} from "./userProposal";
import {University} from "./university";
import {Career} from "./career";
import {Proposal} from "./proposal";

export class User {
  id: number;
  email: string;
  name: string;
  lastName: string;
  birthDate: Date;
  gender: Gender;
  passwordHash: string;
  university: University;
  career: Career;
  bio: string;
  contactNumber: string;
  role: Role;
  interestedProperties: Property[]; //GUEST
  userProposals: UserProposal[]; //GUEST
  ownedProperties: Property[]; //HOST
  hostProposals: Proposal[]; //HOST
}

export type Role = 'ROLE_GUEST' | 'ROLE_HOST';
export const Role = {
  Guest: 'ROLE_GUEST' as Role,
  Host: 'ROLE_HOST' as Role
};

export type Gender = 'MALE' | 'FEMALE' | 'OTHER';
export const Gender = {
  Male: 'MALE' as Gender,
  Female: 'FEMALE' as Gender,
  Other: 'OTHER' as Gender
};


export interface SignUpForm {
  email: string;
  password: string;
  repeatPassword: string;
  name:string ;
  lastName: string;
  phoneNumber: number;
  birthDate: string;
  role: number;
  universityId: number;
  careerId: number;
  bio: string;
  gender: number;
}