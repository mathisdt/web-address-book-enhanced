import {Person} from './Person';

export interface Family {
  id: number | null;
  lastUpdate: string | null;
  lastName: string | null;
  street: string | null;
  postalCode: string | null;
  city: string | null;
  contact1: string | null;
  contact2: string | null;
  contact3: string | null;
  remarks: string | null;
  members: Person[] | null | undefined;
}
