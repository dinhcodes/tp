---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# HallLedger Developer Guide

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Acknowledgements

* HallLedger’s demerit rule catalogue is adapted from the NUS Office of Student Affairs **Demerit Point Structure (DPS) for Breach of Housing Agreement**, dated 9 January 2026.
* This project is based on the **AddressBook-Level3 (AB3)** codebase from [se-education/addressbook-level3](https://github.com/se-edu/addressbook-level3).
* `dinhcodes` used GitHub Copilot to assist with parts of the implementation, including code completion, brainstorming class/architecture ideas, and some low-level styling or boilerplate tasks, but all generated content was reviewed and understood before being kept in the project.

--------------------------------------------------------------------------------------------------------------------

## Setting up, getting started

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## Design

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="300" />

The **Architecture Diagram** above gives a high-level overview of HallLedger.

HallLedger follows the standard AB3-style layered architecture. The main components are:

* [**`UI`**](#ui-component): handles the user interface.
* [**`Logic`**](#logic-component): parses and executes user commands.
* [**`Model`**](#model-component): stores the in-memory application state.
* [**`Storage`**](#storage-component): reads and writes data to disk.
* [**`Commons`**](#common-classes): contains utility classes shared by multiple components.

`Main` and `MainApp` are responsible for starting the app, initializing the components in the correct order, and shutting them down cleanly when the app exits.

The *Sequence Diagram* below shows how the main components interact when the user executes a delete command.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="600" />

Each of the four main components:

* defines its API in an interface with the same name as the component,
* exposes its functionality through that interface,
* and interacts with other components through interfaces rather than direct implementation dependencies.

This helps to keep the system modular and easier to maintain.

### UI component

**API**: [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" width="900" alt="Structure of the UI Component" />

The UI consists of a `MainWindow` made up of multiple UI parts such as:
* `CommandBox`
* `ResultDisplay`
* `TabSection`
* `ListSection`
* `StatusBarFooter`

All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures the common behavior of visible GUI components.

The UI uses the **JavaFX** framework. The layout of each UI part is defined in a corresponding `.fxml` file under `src/main/resources/view`. For example, the layout of `MainWindow` is defined in `MainWindow.fxml`.

The `UI` component:
* executes user commands through the `Logic` component,
* observes model state so the displayed data can be updated automatically,
* depends on some classes in the `Model` component because it displays `Person` data,
* and includes resident-specific tabs such as profile and demerit-record views.

This design keeps the UI modular while allowing resident information to be shown in multiple focused views.

### Logic component

**API**: [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

<puml src="diagrams/LogicClassDiagram.puml" width="550" />

The *Sequence Diagram* below illustrates the interactions within the `Logic` component for a delete command.

<puml src="diagrams/DeleteSequenceDiagram.puml" width="800" alt="Interactions Inside the Logic Component for a delete command" />

The `Logic` component is responsible for:
1. receiving raw user input,
2. identifying the command word,
3. dispatching to the appropriate parser,
4. constructing the corresponding command object,
5. executing that command,
6. and returning a `CommandResult` to the UI.

HallLedger extends the usual AB3 command flow with resident-management features such as:
* `TagCommand`
* `RemarkCommand`
* `DemeritCommand`
* `DemeritListCommand`

At a high level:
* `AddressBookParser` determines which command parser should be used,
* the specific parser validates arguments and creates a command object,
* the command executes using the `Model`,
* and the result is returned to the `UI`.

<box type="info" seamless>

The logic component remains highly testable because parsing and execution are split into separate classes.

</box>

### Model component

**API**: [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="700" />

The `Model` component:

* stores HallLedger’s resident data as `Person` objects,
* stores the currently displayed resident list as a filtered `ObservableList<Person>`,
* stores the currently selected `Person` for UI synchronization,
* stores a `UserPrefs` object representing user preferences,
* stores a `FilterDetails` object representing the most recent resident filter,
* and does not depend on the `UI`, `Logic`, or `Storage` components.

HallLedger’s core domain entity is `Person`, which represents a resident. In addition to basic contact information, a resident can also store:
* typed tags such as year, major, and gender,
* a resident-level remark,
* and demerit incidents used to compute the total demerit points.

This keeps HallLedger’s domain model centered around hall-resident administration rather than a generic contact list.

### Storage component

**API**: [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component:

* saves resident data and user preference data in JSON format,
* reads them back into the corresponding in-memory objects,
* depends on some classes in the `Model` component because it persists model data,
* and is responsible for preserving HallLedger-specific resident information such as tags, remarks, and demerit incidents.

This allows HallLedger to persist hall-administration data without coupling persistence logic to the model or UI layers.

### Common classes

Classes used by multiple components are placed in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## Implementation

This section describes noteworthy implementation details of selected HallLedger features.

### Resident management workflow

HallLedger follows the standard AB3 command workflow for resident-management operations such as `add`, `edit`, `delete`, `tag`, `remark`, and `demerit`.

At a high level, the flow is:

1. the user enters a command in the command box,
2. the `UI` forwards the raw command string to `Logic`,
3. `Logic` dispatches to the correct parser,
4. the parser validates the input and creates the corresponding command object,
5. the command executes against the `Model`,
6. and the resulting updates are reflected in the resident list and relevant tabs.

This structure keeps the command-handling logic centralized and consistent across features.

For HallLedger specifically, this is useful because many resident-management features share the same overall interaction pattern:
* identify the target resident,
* validate hall-relevant input,
* update resident data,
* and refresh the UI accordingly.

This common workflow helps new features fit cohesively into the product without requiring separate execution pipelines for each feature.

### Demerit point tracking

HallLedger stores demerit incidents as resident-level records instead of storing only a mutable running total.

Each demerit incident records:
* the DPS rule index,
* the rule title,
* the offence number for that resident and rule,
* the points applied for that occurrence,
* and an optional remark.

A resident’s total demerit points are **derived** by summing the points applied across all stored demerit incidents. This avoids duplicated derived state and keeps the resident’s demerit history auditable.

The demerit feature is split into two user-facing commands:

* `demeritlist` — displays the indexed demerit rule catalogue and point tiers.
* `demerit` — applies an indexed demerit rule to a resident identified by student ID.

#### Rationale for the current design

A resident may commit the same rule multiple times, and the DPS applies different point tiers based on repeated occurrences. Hence, storing only a running total would lose important context such as:
* which rule was applied,
* how many times it had already been committed by that resident,
* and what exact points were awarded for that occurrence.

By storing incidents individually, HallLedger can:
* reconstruct total demerit points at any time,
* preserve a readable incident history,
* and support future enhancements such as warnings, alerts, or administrative review.

#### Current scope note

HallLedger currently records resident demerit incidents and computes accumulated totals. It does **not** yet automatically enforce semester-based or lifetime housing sanctions tied to DPS thresholds.

### Demerit records UI

HallLedger provides a dedicated **Demerit Records** tab for the currently selected resident.

This tab displays:
* the resident’s accumulated total demerit points,
* the resident’s demerit incident history,
* the rule description,
* incident remarks,
* and the points applied for each incident.

The tab updates when the selected resident changes.

This design was chosen because:
* hall administrators often need both the current total and the incident history,
* showing only a running total would hide important context,
* and separating demerit records into a dedicated tab keeps the interface organized.

--------------------------------------------------------------------------------------------------------------------

## Appendix: Requirements

### Product scope

HallLedger is a desktop application for **Resident Assistants (RAs)** and other hall administrators who need to manage resident records in NUS halls efficiently.

Beyond basic resident record management, HallLedger supports common hall-administration workflows such as:
* organizing residents by typed tags,
* recording short operational remarks,
* and tracking demerit incidents in a structured way.

HallLedger is **not** intended to replace university-wide housing allocation systems, payment systems, or institutional access-control systems.

**Target user profile**

The target user:
* manages many residents,
* frequently needs quick access to resident information,
* prefers keyboard-driven workflows where possible,
* and wants a local desktop tool rather than scattered spreadsheets or notes.

**Value proposition**

HallLedger helps hall administrators manage resident records faster and with fewer errors than spreadsheets or ad hoc note-taking workflows, while keeping hall-specific information such as tags, remarks, and demerit incidents in one place.

---

### User stories

Priorities: High (`***`), Medium (`**`), Low (`*`)

| Priority | As a ... | I want to ... | So that I can ... |
| --- | --- | --- | --- |
| `***` | hall administrator | add resident records | keep the resident list up to date |
| `***` | hall administrator | edit resident records | correct outdated or incorrect information |
| `***` | hall administrator | delete resident records | remove residents who are no longer relevant |
| `***` | hall administrator | view all residents | get a quick overview of the current resident list |
| `***` | hall administrator | find residents using key fields | locate residents quickly |
| `***` | hall administrator | tag residents by year, major, and gender | organize residents in hall-relevant ways |
| `***` | hall administrator | add remarks to residents | store short operational notes |
| `***` | hall administrator | add demerit incidents to residents | keep track of rule breaches |
| `**` | hall administrator | view a resident’s demerit history | understand how the current total was formed |
| `**` | hall administrator | view the demerit rule list | apply the correct rule consistently |

---

### Use cases

For all use cases below, the **System** is HallLedger and the **Actor** is the hall administrator unless stated otherwise.

#### UC1: Add a resident

**MSS**
1. User enters a valid `add` command with the required resident details.
2. HallLedger adds the resident.
3. HallLedger shows a success message.

Use case ends.

**Extensions**
* 1a. Some input fields are invalid.
  * 1a1. HallLedger rejects the command and shows an error message.
  * Use case ends.
* 1b. The resident conflicts with an existing resident.
  * 1b1. HallLedger rejects the command and shows an error message.
  * Use case ends.

#### UC2: Edit a resident

**MSS**
1. User enters a valid `edit` command for an existing resident.
2. HallLedger updates the resident.
3. HallLedger shows a success message.

Use case ends.

**Extensions**
* 1a. The target student ID does not exist.
  * 1a1. HallLedger shows an error message.
  * Use case ends.
* 1b. The edit contains invalid values.
  * 1b1. HallLedger rejects the command and shows an error message.
  * Use case ends.

#### UC3: Find residents

**MSS**
1. User enters a `find` command using one or more supported fields.
2. HallLedger filters the resident list.
3. HallLedger shows the filtered list.

Use case ends.

#### UC4: Tag a resident

**MSS**
1. User enters a valid `tag` command with one or more supported tag values.
2. HallLedger updates the resident’s tags.
3. HallLedger shows a success message.

Use case ends.

#### UC5: Add or update a resident remark

**MSS**
1. User enters a valid `remark` command.
2. HallLedger updates the resident’s remark.
3. HallLedger shows a success message.

Use case ends.

#### UC6: View demerit rules

**MSS**
1. User enters `demeritlist`.
2. HallLedger shows the available demerit rules.

Use case ends.

#### UC7: Add a demerit incident

**MSS**
1. User enters a valid `demerit` command with a resident ID and rule index.
2. HallLedger determines the offence number for that resident and rule.
3. HallLedger computes the points applied for that occurrence.
4. HallLedger records the incident.
5. HallLedger shows a success message and updates the resident’s demerit records.

Use case ends.

**Extensions**
* 1a. The resident does not exist.
  * 1a1. HallLedger shows an error message.
  * Use case ends.
* 1b. The demerit rule index does not exist.
  * 1b1. HallLedger shows an error message.
  * Use case ends.

#### UC8: Delete a resident

**MSS**
1. User enters a valid `delete` command.
2. HallLedger opens a confirmation dialog.
3. User confirms the deletion.
4. HallLedger deletes the resident.
5. HallLedger shows a success message.

Use case ends.

**Extensions**
* 2a. User cancels the deletion.
  * 2a1. HallLedger aborts deletion and shows a cancellation message.
  * Use case ends.
* 1a. The command format is invalid.
  * 1a1. HallLedger shows an error message instead of opening the dialog.
  * Use case ends.

---

### Non-Functional Requirements

1. HallLedger should work on mainstream desktop operating systems that support Java 17.
2. HallLedger should remain usable for a typical hall-level resident dataset.
3. A fast typist should be able to complete common administrative tasks efficiently through commands.
4. HallLedger should store data locally in a human-editable file.
5. HallLedger should work without requiring internet access during normal usage.
6. HallLedger should remain a single-user application.

---

### Glossary

* **Resident**: A hall resident stored in HallLedger.
* **RA**: Resident Assistant.
* **Student ID**: The identifier used by HallLedger to target a resident.
* **Remark**: A short resident-level operational note.
* **Demerit incident**: A single recorded rule breach applied to a resident.
* **Demerit rule**: A rule in the demerit catalogue that can be applied using `demerit`.
* **DPS**: Demerit Point Structure used as the source reference for HallLedger’s demerit rules.

--------------------------------------------------------------------------------------------------------------------

## Appendix: Instructions for Manual Testing

Given below are instructions to test the app manually.

<box type="info" seamless>

These instructions provide a starting point. Testers are expected to do exploratory testing beyond them.

</box>

### Launch and shutdown

1. Initial launch
   1. Download the jar file into an empty folder.
   2. Run the app.
      Expected: HallLedger starts with sample data.

2. Closing and reopening
   1. Close the app.
   2. Launch it again.
      Expected: previously saved data and preferences are restored.

---

### Adding a resident

1. Valid add
   1. Enter
      `add n=John Doe p=+6598765432 e=johnd@example.com i=A1234567X r=15R ec=+65 91234567`
   2. Verify that the resident is added and the result box shows success.

2. Invalid add
   1. Enter an add command with an invalid field value.
   2. Verify that HallLedger rejects the command.

---

### Finding residents

1. Enter
   `find n=John`
2. Verify that the list is filtered appropriately.

3. Enter
   `find y=2 m=Computer Science`
4. Verify that the list is filtered accordingly.

---

### Tagging a resident

1. Enter
   `tag i=A1234567X y=2 m=Computer Science g=he/him`
2. Verify that the target resident’s tags are updated.

---

### Adding a remark

1. Enter
   `remark i=A1234567X rm=Peanut allergy`
2. Verify that the resident remark is updated.

---

### Demerit features

1. Show demerit rules
   1. Enter `demeritlist`
   2. Verify that HallLedger shows the supported rule list.

2. Add a demerit incident
   1. Enter
      `demerit i=A1234567X di=18 rm=Visitor during quiet hours`
   2. Verify that:
      * the command succeeds,
      * the success message includes the remark,
      * the resident’s demerit tab updates,
      * and the total demerit points increase correctly.

3. Repeated offence
   1. Enter the same `demerit` command again.
   2. Verify that the offence number and points reflect a repeated offence for that rule.

4. Invalid rule
   1. Enter
      `demerit i=A1234567X di=999`
   2. Verify that HallLedger rejects the command.

---

### Deleting a resident

1. Enter
   `delete i=A1234567X`
2. Verify that a confirmation dialog appears.
3. Cancel the deletion.
   Expected: the resident remains.
4. Repeat and confirm the deletion.
   Expected: the resident is removed.

--------------------------------------------------------------------------------------------------------------------

## Appendix: Planned Enhancements

Team size: 5

1. Make demerit-threshold handling more explicit: HallLedger currently computes accumulated demerit totals but does not yet surface warnings or administrative prompts when a resident crosses important DPS-related thresholds. We plan to add clearer threshold-based feedback tied to the resident’s accumulated total.

2. Improve robustness when users manually edit `addressbook.json`: HallLedger currently expects manually edited JSON data to remain logically valid. We plan to detect and explain more invalid manual edits instead of only failing at load time.

3. Refine demerit-table behavior for extremely narrow layouts: The demerit tab currently keeps detailed incident information visible, but very narrow layouts can still feel cramped. We plan to improve the layout behavior further while keeping the incident history readable.

4. Make tag-validation error messages more specific: HallLedger currently rejects invalid tag inputs, but some error messages can still be made more precise. We plan to state more clearly which tag field failed validation and why.

5. Improve delete-confirmation feedback: HallLedger currently confirms deletion using a modal dialog. We plan to make the feedback around cancellation and confirmation even clearer so users can more easily distinguish between aborted and completed deletion flows.

6. Make repeated-demerit feedback more informative: HallLedger currently shows the applied rule, remark, points added, and updated total. We plan to also surface the offence number more prominently in the success feedback for easier verification.

7. Improve messaging for invalid resident targeting: Commands such as `edit`, `remark`, and `demerit` reject nonexistent student IDs. We plan to standardize those error messages further so the reason for failure is even clearer to users.

8. Improve save-failure guidance for write-protected folders: HallLedger currently depends on write access to its home folder. We plan to provide clearer user-facing guidance when saving fails due to file-permission issues.

9. Improve documentation alignment for platform-specific behavior: HallLedger currently documents known issues such as dialog behavior and write-protected folders. We plan to keep refining the UG/DG wording and screenshots so platform-specific caveats remain easy to understand.

10. Improve handling of very long resident remarks: HallLedger currently stores and displays resident remarks, but very long remarks may be less readable in some UI contexts. We plan to improve readability for unusually long remarks without changing the underlying data model.
