import {Component, effect, input, InputSignal, OnChanges, signal, SimpleChanges, WritableSignal} from '@angular/core';
import {Family} from '../model/Family';
import {EmailBatch} from '../model/EmailBatch';

@Component({
  selector: 'email-component',
  imports: [],
  templateUrl: './email.component.html',
  styleUrl: './email.component.css'
})
export class EmailComponent {
  families: InputSignal<Family[]> = input.required<Family[]>();
  batchSize: WritableSignal<number> = signal<number>(0);
  batches: WritableSignal<EmailBatch[]> = signal<EmailBatch[]>(Array<EmailBatch>());

  private emailRegEx: RegExp = new RegExp("^\\S+@\\S+\\.\\S+$");

  constructor() {
    effect(() => {
      console.log(`detected ${this.families().length} families`)
      this.calculateBatches(this.batchSize());
    });
  }

  extractEmailAddresses(): string[] {
    let result: string[] = Array<string>();
    for (const family of this.families()) {
      if (this.isEmailAddress(family.contact1)) {
        result.push(<string>family.contact1);
      }
      if (this.isEmailAddress(family.contact2)) {
        result.push(<string>family.contact2);
      }
      if (this.isEmailAddress(family.contact3)) {
        result.push(<string>family.contact3);
      }
      if (family.members) {
        for (const person of family.members) {
          if (this.isEmailAddress(person.contact1)) {
            result.push(<string>person.contact1);
          }
          if (this.isEmailAddress(person.contact2)) {
            result.push(<string>person.contact2);
          }
          if (this.isEmailAddress(person.contact3)) {
            result.push(<string>person.contact3);
          }
        }
      }
    }
    // no duplicates in output:
    return result.filter(function (elem, index, self) {
      return index === self.indexOf(elem);
    });
  }

  private isEmailAddress(contact: string | null): boolean {
    return contact !== null && this.emailRegEx.test(contact);
  }

  calculateBatches(batchSize: number = 0): void {
    console.log(`calculating batches with batch size ${batchSize}`)
    this.batchSize.update(oldBatchSize => batchSize);
    const emailAddresses: string[] = this.extractEmailAddresses();
    const result: EmailBatch[] = Array<EmailBatch>();
    if (batchSize === 0) {
      result.push({
        index: 0,
        batchSize: 0,
        emails: emailAddresses
      });
    } else {
      for (let i = 0; i < Math.ceil(emailAddresses.length / batchSize); i++) {
        const startIndex = batchSize * i;
        const endIndex = batchSize * (i+1) > emailAddresses.length ? emailAddresses.length : batchSize * (i+1);
        result.push({
          batchSize: batchSize,
          index: i,
          emails: emailAddresses.slice(startIndex, endIndex)
        });
      }
    }
    console.log(`calculated batches with batch size ${batchSize} => ${result.length} batches, first has ${result[0].emails.length} email addresses`)
    this.batches.set(result);
  }

  protected readonly parseInt = parseInt;
}
