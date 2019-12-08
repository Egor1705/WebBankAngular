import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {FormControl, FormGroup, Validators} from '@angular/forms';

//import {Bill} from './bill';




const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/x-www-form-urlencoded'
  })
};

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
 
})
export class AppComponent {
//  bill: Bill;
  title = 'Web Developer Blog';
  loginValid: Object = '';
  fail: boolean = false;
  takeValid:Object = '';
  putValid:Object = '';
  openValid: Object = '';
  closeValid: Object = '';
  sumValid:Object='';
  fail1:boolean = false;
  fail2:boolean = false;
  fail3:boolean = false;
  failPut:boolean = false;
  failTake:boolean = false;
  failSum:boolean = false;
  submitted = false;
  submitted1 = false;
  submitted2 = false;
  submittedPut = false;
  submittedTake = false;
  submittedSum = false;

  loginUrl = "http://localhost:5010/login";
  outputUrl = "http://localhost:5010/output";
  openUrl = "http://localhost:5010/openBill";
  closeUrl = "http://localhost:5010/closeBill";
  putUrl = "http://localhost:5010/putMoney";
  takeUrl = "http://localhost:5010/takeMoney";
sumUrl = "http://localhost:5010/showSum";
   public constructor(public http: HttpClient) {
   }
showSum(id){
  let params = new HttpParams().set("sumOfBill",id);
  this.http.get(this.sumUrl,{params: params}).subscribe(
    data => {
      this.sumValid = data;
      if(this.sumValid["response"]=='failSum'){
        this.failSum = true;
      }
      else if(this.sumValid["response"] == 'failSum'){
       this.failSum = true;
     }

     else {
       this.failSum = false;
       this.submittedSum = true;
     }
     console.log(this.sumValid);
     //this.getSum();
   },
   error => {
     console.log(error);
   }
    
  )


}


   takeMoney(id,sum){
     let params = new HttpParams().set("take",id).set("sum",sum);
     this.http.get(this.takeUrl,{params: params}).subscribe(
       data => {
         this.takeValid = data;
         if(this.takeValid["response"]=='failTake'){
           this.failTake = true;
         }
         else if(this.takeValid["response"] == 'failTake'){
          this.failTake = true;
        }
        else if(this.takeValid["response"] == 'failTake'){
          this.failTake = true;
        }
        else {
          this.failTake = false;
          this.submittedTake = true;
        }
        console.log(this.takeValid);
        //this.getSum();
      },
      error => {
        console.log(error);
      }
       
     )
   }
   putMoney(id,sum){
       let params = new HttpParams().set("put", id).set("sum", sum); //Create new HttpParams
   this.http.get(this.putUrl, {params: params}).subscribe(
     data => {
       this.putValid = data;
       if (this.putValid["response"] == 'failput') {
         this.failPut = true;
       }
       else if(this.putValid["response"] == 'failput'){
        this.failPut = true;
      }

       else {
         this.failPut = false;
         this.submittedPut = true;
       }
       console.log(this.putValid);
       //this.getSum();
     },
     error => {
       console.log(error);
     }
   );
 }




checkClose(id){
   let params = new HttpParams().set("close",id);
     this.http.get(this.closeUrl,{params:params}).subscribe(
      data => {
        this.closeValid = data;
        if (this.closeValid["response"] == 'fail2') {
          this.fail2 = true;
        }
        else if(this.closeValid["response"] == 'fail2'){
          this.fail2 = true;
        }
       
        else {
          this.fail2 = false;
          this.submitted2 = true;
        }
        console.log(this.closeValid);
        //this.getSum();
      },
      error => {
        console.log(error);
      }
     )
}

   checkOpen(id){
     let params = new HttpParams().set("open",id);
     this.http.get(this.openUrl,{params:params}).subscribe(
      data => {
        this.openValid = data;
        if (this.openValid["response"] == 'fail1') {
          this.fail1 = true;
        }
       
        else {
          this.fail1 = false;
          this.submitted1 = true;
        }
        console.log(this.openValid);
        //this.getSum();
      },
      error => {
        console.log(error);
      }
     )
   }
   
   checkLogin(id, pwd) {
    
    let params = new HttpParams().set("log", id).set("pwd", pwd); //Create new HttpParams
    this.http.get(this.loginUrl, {params: params}).subscribe(
      data => {
        this.loginValid = data;
        if (this.loginValid["response"] == 'fail') {
          this.fail = true;
        }
        else {
          this.fail = false;
          this.submitted = true;
        }
        console.log(this.loginValid);
        //this.getSum();
      },
      error => {
        console.log(error);
      }
    );
  }

      }
