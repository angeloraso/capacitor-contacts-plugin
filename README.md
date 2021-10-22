# capacitor-contacts-plugin

Capacitor plugin to access the phone book

## Install

```bash
npm install capacitor-contacts-plugin
npx cap sync
```

## API

<docgen-index>

* [`checkPermissions()`](#checkpermissions)
* [`requestPermissions()`](#requestpermissions)
* [`getContacts()`](#getcontacts)
* [`createContact(...)`](#createcontact)
* [`addToExistingContact(...)`](#addtoexistingcontact)
* [`deleteContact(...)`](#deletecontact)
* [`getGroups()`](#getgroups)
* [`getContactGroups()`](#getcontactgroups)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### checkPermissions()

```typescript
checkPermissions() => any
```

**Returns:** <code>any</code>

--------------------


### requestPermissions()

```typescript
requestPermissions() => any
```

**Returns:** <code>any</code>

--------------------


### getContacts()

```typescript
getContacts() => any
```

**Returns:** <code>any</code>

--------------------


### createContact(...)

```typescript
createContact(data: { name?: string; number: string; }) => any
```

| Param      | Type                                            |
| ---------- | ----------------------------------------------- |
| **`data`** | <code>{ name?: string; number: string; }</code> |

**Returns:** <code>any</code>

--------------------


### addToExistingContact(...)

```typescript
addToExistingContact(data: { name?: string; number: string; }) => any
```

| Param      | Type                                            |
| ---------- | ----------------------------------------------- |
| **`data`** | <code>{ name?: string; number: string; }</code> |

**Returns:** <code>any</code>

--------------------


### deleteContact(...)

```typescript
deleteContact(data: { contactId: string; }) => any
```

| Param      | Type                                |
| ---------- | ----------------------------------- |
| **`data`** | <code>{ contactId: string; }</code> |

**Returns:** <code>any</code>

--------------------


### getGroups()

```typescript
getGroups() => any
```

**Returns:** <code>any</code>

--------------------


### getContactGroups()

```typescript
getContactGroups() => any
```

**Returns:** <code>any</code>

--------------------


### Interfaces


#### PermissionStatus

| Prop          | Type                 |
| ------------- | -------------------- |
| **`granted`** | <code>boolean</code> |


#### Contact

| Prop                   | Type                |
| ---------------------- | ------------------- |
| **`contactId`**        | <code>string</code> |
| **`displayName`**      | <code>string</code> |
| **`phoneNumbers`**     | <code>{}</code>     |
| **`emails`**           | <code>{}</code>     |
| **`photoThumbnail`**   | <code>string</code> |
| **`organizationName`** | <code>string</code> |
| **`organizationRole`** | <code>string</code> |
| **`birthday`**         | <code>string</code> |


#### PhoneNumber

| Prop         | Type                |
| ------------ | ------------------- |
| **`label`**  | <code>string</code> |
| **`number`** | <code>string</code> |


#### EmailAddress

| Prop          | Type                |
| ------------- | ------------------- |
| **`label`**   | <code>string</code> |
| **`address`** | <code>string</code> |


#### Group

| Prop              | Type                |
| ----------------- | ------------------- |
| **`groupId`**     | <code>string</code> |
| **`accountType`** | <code>string</code> |
| **`accountName`** | <code>string</code> |
| **`title`**       | <code>string</code> |

</docgen-api>
