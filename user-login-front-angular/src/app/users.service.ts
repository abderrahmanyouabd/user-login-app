import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private baseUrl = "http://localhost:8080";

  constructor(private http: HttpClient) { }

  async login(email: string, password: string): Promise<any> {

    const url = this.baseUrl + "/auth/login";
    try {
      const [response] = await Promise.all([this.http.post<any>(url, {email, password})]);
      return response;
    } catch (error){
      throw error;
    }
  }

  async register(userData: any): Promise<any> {

    const url = this.baseUrl + "/auth/register";
    // const headers = new HttpHeaders({
    //   'Authorization': `Bearer ${token}`
    // })
    try {
      const [response] = await Promise.all([this.http.post(url, userData)]);
      return response;
    } catch (error){
      throw error;
    }
  }


  async getAllUsers(token:string):Promise<any>{
    const url = `${this.baseUrl}/admin/get-all-users`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response =  this.http.get<any>(url, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  async getYourProfile(token:string):Promise<any>{
    const url = `${this.baseUrl}/user-admin/profile`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response =  this.http.get<any>(url, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  async getUsersById(userId: string, token:string):Promise<any>{
    const url = `${this.baseUrl}/admin/users/${userId}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try{
      const response =  this.http.get<any>(url, {headers}).toPromise()
      return response;
    }catch(error){
      throw error;
    }
  }

  // AUTHENTICATION METHODS
  logOut():void{
    if(typeof localStorage !== 'undefined'){
      localStorage.removeItem('token')
      localStorage.removeItem('role')
    }
  }

  isAuthenticated(): boolean {
    if(typeof localStorage !== 'undefined'){
      const token = localStorage.getItem('token');
      return !!token;
    }
    return false;

  }

  isAdmin(): boolean {
    if(typeof localStorage !== 'undefined'){
      const role = localStorage.getItem('role');
      return role === 'ADMIN'
    }
    return false;

  }

  isUser(): boolean {
    if(typeof localStorage !== 'undefined'){
      const role = localStorage.getItem('role');
      return role === 'USER'
    }
    return false;

  }
}
