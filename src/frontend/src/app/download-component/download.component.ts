import {Component} from '@angular/core';

@Component({
  selector: 'download-component',
  imports: [],
  templateUrl: './download.component.html',
  styleUrl: './download.component.css'
})
export class DownloadComponent {

  export() {
    window.open("http://localhost:8080/data/report", "_blank");
  }
}
