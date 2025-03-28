import {Component} from '@angular/core';
import {environment} from '../../environments/environment';

@Component({
  selector: 'download-component',
  imports: [],
  templateUrl: './download.component.html'
})
export class DownloadComponent {
  baseUrl: string = environment.backendUrl;

  export() {
    window.open(`${this.baseUrl}/report`, "_blank");
  }
}
