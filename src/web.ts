/* eslint-disable import/order */
import { WebPlugin } from '@capacitor/core';
import type { Contact, ContactsPlugin, Group, PermissionStatus } from './definitions';

export class ContactsWeb extends WebPlugin implements ContactsPlugin {
  async getPermissions(): Promise<PermissionStatus> {
    return {granted: false};
  }

  async requestPermissions(): Promise<PermissionStatus> {
    return {granted: false};
  }

  async getContacts(): Promise<{ contacts: Contact[]}> {
    return { contacts: []};
  }
  
  async createContact(data: {nam?: string, number: string}): Promise<void> {
    console.log('CONTACTS', data);
    return;
  }

  async addToExistingContact(data: {nam?: string, number: string}): Promise<void> {
    console.log('CONTACTS', data);
    return;
  }

  async deleteContact(data: {contactId: string}): Promise<void> {
    console.log('CONTACT ID', data);
    return;
  }

  async getGroups(): Promise<{ groups: Group[]}> {
    return { groups: []};
  }

  async getContactGroups(): Promise<{[key: string]: Group[]}> {
    return { contactGroups: []};
  }
}
