# e-conomic Visma | Android App tech test

(APK included: **app-debug.apk**)

<img src="screenshots/invoices_list-empty.jpg" alt="drawing" width="180"/><img src="screenshots/invoices_list.jpg" alt="drawing" width="180"/>
<img src="screenshots/save_invoice.jpg" alt="drawing" width="180"/><img src="screenshots/invoice_details.jpg" alt="drawing" width="180"/>

# Requirements
- [x] Take photos of my expenses (receipts or invoices).
- [x] Add information about the receipt (date, total, currency, etc) and store it locally
- [x] Access the history of the photos taken (should be available offline) with the data I
inputted
- [x] The app should work even if you are offline and should maintain its state after closing
- [x] The solution must be thread safe and should be robust enough to handle large sets
of data.

# Modules
The app is organized into a multi-module structure to promote separation of concerns, following clean architecture.
- **app**: Application entry point and navigation logic.
- **core**: Data, domain and ui components common to the project.
- **database**: Local database.
- **feature/invoice**: Invoice feature. Implements "Invoices List Screen", "Save Invoice Screen", "Invoice Details Screen", "Invoice Photo Screen" with all their layers (domain, data, presentation).  

# Features
Each new feature should:
- Be implemented in their own module, in the **'feature'** module.
- Have the domain layer in a **'domain'** package, data layer in a **'data'** package, and presentation layer in a **'ui'** package.
- Inject dependencies in a **'di'** (dependency injection) package.

## Domain Layer
In the domain layer (**'domain'**):
- Implement interactors, responsible for serving the use cases as their methods.
- House the domain models.
- Declare the repositories, which are responsible for managing the data sources to provide the required data in the interactors. Actual implementation is done in the **'data'** layer.

## Data Layer
In the data layer (**'data'**):
- Implement the repositories declared in the **'domain'**.
- House the data sources of the feature.
- House the data models of the feature.

## Presentation Layer
In the presentation layer (**'ui'**):
- Each screen for the feature is implemented in its own package.
For example, the invoice feature has 4 packages: **'details'** (Invoice Details Screen), **'list'** (Invoices List Screen), **'photo'** (Invoice Photo Screen), and **'save'** (Invoice Save Screen).
- MVI architecture is applied.
- Jetpack Compose is used.
- Each screen has a contract interface that declares its state, ui events, one-time events, output and destination.

## Example: Invoice feature
- /feature
  - /invoice
    - /data
      - /local
        - /datasource
        - /model
      - /repository
    - /di
    - /domain
      - /interactor
      - /model
      - /repository
    - /ui
      - /details
      - /list
      - /photo
      - /save

# Improvements
- Created unit tests for the **'InvoiceInteractorImpl'** so you may have an idea of how I do it, but still need to increase tests coverage of the project.
- Use a date picker in the "Invoice Save Screen" for the date field, and display dates in a more user friendly format.
- Support multiple currencies and display money in a more user friendly format.
- In the "Invoice Save Screen", move focus from one field to the next.
- Implement filters in the "Invoices List Screen". I have even implemented some of it already, but couldn't find the time to finish it, so I've left some TODOs in the code.
- Implement invoice's edit and delete functionality (assuming the user would be allowed to edit and delete invoices). The "Invoice Save Screen" already supports editing, and upserting invoices in the local database should already work, but the "Invoice Details Screen" is missing the implementation.
- Adapt layout according to screen size (landscape or tablets).
- Centralise dependencies declaration and clean modules build.gradle file.

# Software Engineer
Matheus de Oliveira Castro (matheuscastro.dev {at} gmail {dot} com)