---
layout: default.md
title: "Fuzzy Matching Details"
pageNav: 3
---

# Fuzzy Matching Details

This page explains how Hall Ledger decides whether a resident matches your `find` input.

## 1. Quick idea

Hall Ledger uses **fuzzy matching** to make searches more forgiving.
Your keyword is treated as a match when any of the following is true (all comparisons ignore uppercase/lowercase):

1. **Exact match** — your keyword is the same as the resident's value.
2. **Substring match** — your keyword appears inside the resident's value.
3. **Typo tolerance** — your keyword is 4 or more characters long, and differs from the resident's value by at most
   2 edits (insertions, deletions, or substitutions).

<box type="info" seamless>

For short keywords (3 characters or fewer), only rules 1 and 2 apply — typo tolerance is disabled to avoid too many
unrelated results.

</box>

**Examples:**

| You type | Resident value | Fuzzy Match? | Why                                                             |
|----------|----------------|--------------|-----------------------------------------------------------------|
| `ali`    | `Alice`        | Yes          | `ali` is a substring of `Alice`.                                |
| `ALEX`   | `alex`         | Yes          | Exact match (case-insensitive).                                 |
| `sitten` | `kitten`       | Yes          | Only 1 edit apart — within the typo tolerance.                  |
| `ann`    | `ana`          | No           | Too short for typo tolerance, and not an exact/substring match. |

## 2. Which fields use which matching style?

| Prefix | Field             | Matching style            | Example                                                                                |
|--------|-------------------|---------------------------|----------------------------------------------------------------------------------------|
| `n=`   | Name              | Fuzzy                     | `n=alex` can match `Alex Tan`.                                                         |
| `p=`   | Phone             | Fuzzy                     | `p=9123` can match `+65 91234567`.                                                     |
| `e=`   | Email             | Fuzzy                     | `e=@gmail` can match `alex@gmail.com`.                                                 |
| `i=`   | Student ID        | Exact (case-insensitive)* | `i=a1234567x` matches `A1234567X`; `i=1234` does **not** match `A1234567X`.            |
| `ec=`  | Emergency contact | Fuzzy                     | `ec=9876` can match `+65 98765432`.                                                    |
| `r=`   | Room number       | Fuzzy                     | `r=12` can match `12A`.                                                                |
| `y=`   | Year tag          | Exact (normalised)**      | `y=1` matches a resident tagged with year `1`.                                         |
| `m=`   | Major tag         | Fuzzy                     | `m=computer sci` can match `Computer Science`.                                         |
| `g=`   | Gender tag        | Exact (normalised)**      | `g=he` matches `he/him`; `g=her` matches `she/her`; `g=they/them` matches `they/them`. |

## 3. Notes on exact-match fields

\* **Student ID** uses exact matching because it is a unique identifier.
Partial or fuzzy matches would produce too many false positives, since many student IDs share similar digit sequences.

<box type="tip" seamless>

**Tip:** Because each Student ID is unique, searching with the full ID (e.g. `find i=A1234567X`) is guaranteed to return
exactly one resident. This is a quick way to pull up a specific resident's profile.

</box>

\** **Year** and **Gender** keywords are **normalised** to their canonical form before matching.
Because the valid values are a small fixed set, the end result is effectively an exact match.
See [Section 4](#4-note-on-year-and-gender-keywords) for details.

## 4. Note on Year and Gender keywords

Hall Ledger only accepts a fixed set of values for Year and Gender tags:

- **Valid year values:** `1`, `2`, `3`, `4`, `5`, `6`
- **Valid gender values:** `he/him`, `she/her`, `they/them`

This is why the Filter Panel uses selection boxes for these two fields.

![Year and Gender Combo Box](images/combo-boxes.png)

**How normalisation works:**

When you use `find` (or the Filter Panel), Hall Ledger normalises your input before matching:

- **Year** — must be exactly one of `1` to `6`. Any other value (e.g. `Y1`, `7`) is invalid.
- **Gender** — shorthand forms such as `he`, `him`, `she`, `her`, `they`, or `them` are automatically expanded to
  their full pronoun form (`he/him`, `she/her`, `they/them`). Any other value (e.g. `male`, `female`) is invalid.

If you enter an invalid year or gender keyword, Hall Ledger will **ignore it and show a warning** — the rest of your
search still runs normally.

![Warning - Ignored values](images/warning-ignored-values-for-year-and-gender.png)

## 5. How multiple filters combine

<box type="info" seamless>

- **Different prefixes** narrow the results — a resident must match **all** specified fields.
    - `find n=Alice y=1` → name matches `Alice` **and** year matches `1`.
    - `find ec=98765432 m=CS` → emergency contact matches `98765432` **and** major matches `CS`.
- **Repeating the same prefix** widens that filter — a resident only needs to match **any one** of those keywords.
    - `find y=2 y=3` → year is `2` **or** `3`.
    - `find n=Alice n=Bob` → name matches `Alice` **or** `Bob`.
    - `find n=Alice n=Bob y=2 y=3` → (name matches `Alice` **or** `Bob`) **and** (year is `2` **or** `3`).

</box>

## 6. Case sensitivity

All `find` matching is case-insensitive.

- `find n=alice` and `find n=ALICE` behave the same.
- `find e=GMAIL` can match `gmail.com`.
