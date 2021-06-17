import { WebPlugin } from '@capacitor/core';
import type { Contact, ContactsPlugin, Group, PermissionStatus } from './definitions';
export declare class ContactsWeb extends WebPlugin implements ContactsPlugin {
    getPermissions(): Promise<PermissionStatus>;
    requestPermissions(): Promise<PermissionStatus>;
    getContacts(): Promise<{
        contacts: Contact[];
    }>;
    createContact(data: {
        nam?: string;
        number: string;
    }): Promise<void>;
    addToExistingContact(data: {
        nam?: string;
        number: string;
    }): Promise<void>;
    deleteContact(data: {
        contactId: string;
    }): Promise<void>;
    getGroups(): Promise<{
        groups: Group[];
    }>;
    getContactGroups(): Promise<{
        [key: string]: Group[];
    }>;
}
