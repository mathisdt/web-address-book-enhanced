import {Component, effect, input, InputSignal, OnChanges, signal, SimpleChanges, WritableSignal} from '@angular/core';
import {Family} from '../family/model/Family';
import {EmailBatch} from './model/EmailBatch';

@Component({
  selector: 'email-component',
  imports: [],
  templateUrl: './email.component.html',
  styleUrl: './email.component.css'
})
export class EmailComponent {
  families = input.required<Family[]>();
  batchSize = signal<number>(0);
  batches = signal<EmailBatch[]>([]);

  private readonly emailRegEx = /^\S+@\S+\.\S+$/;

  constructor() {
    effect(() => {
      this.calculateBatches(this.batchSize());
    });
  }

  extractEmailAddresses(): string[] {
    const emailAddresses: string[] = [];

    for (const family of this.families()) {
      [family.contact1, family.contact2, family.contact3]
        .filter(contact => this.isEmailAddress(contact))
        .forEach(email => emailAddresses.push(email as string));

      if (family.members) {
        for (const person of family.members) {
          [person.contact1, person.contact2, person.contact3]
            .filter(contact => this.isEmailAddress(contact))
            .forEach(email => emailAddresses.push(email as string));
        }
      }
    }

    // use set to eliminate duplicates
    return [...new Set(emailAddresses)];
  }

  private isEmailAddress(contact: string | null): boolean {
    return contact !== null && this.emailRegEx.test(contact);
  }

  calculateBatches(batchSize: number = 0): void {
    this.batchSize.set(batchSize);
    const emailAddresses: string[] = this.extractEmailAddresses();
    const result: EmailBatch[] = [];
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
