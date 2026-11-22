# Mathra — illustrator brief (hand-drawn festival pandas)

**Status:** no festival art is bundled; the app shows the real logo panda with vector accessories until these files exist.

**Goal:** replace the simple vector costume pieces with hand-drawn cartoon panda characters (Santa panda,
New Year / Avurudu panda, etc.) in the same style as the Mathra logo (`app/src/main/res/drawable/mathra_logo.png`).

## Deliverables
One transparent PNG per festival, **1024 x 1024 px**, panda centred with ~8% margin, plus the layered source
file (PSD / Procreate / AI) and a flat logo-style version for the app icon if wanted.
Shown in-app at 88–180 dp, so keep shapes bold and readable when small.

## File names (the app picks them up automatically, no code change)
Put in `app/src/main/res/drawable/` — lowercase, exactly these names:

| File | Character | Used when |
|---|---|---|
| `panda_christmas.png` | Santa panda (suit, hat, sack) | Christmas, 25 Dec |
| `panda_avurudu.png` | New Year panda (Sinhala & Tamil New Year: traditional dress, kiribath/kavum, oil lamp, Avurudu sun) | 13–14 Apr |
| `panda_pongal.png` | Pongal panda (pot boiling over, sugarcane, kolam) | Thai Pongal, 15 Jan |
| `panda_vesak.png` | Vesak panda (lantern / kudu, white clothes) | Vesak, 2 days |
| `panda_poson.png` | Poson panda (lantern / pandal, calm pose) | Poson Poya |
| `panda_poya.png` | Poya panda (white clothes, flowers, full moon) | every other Poya day |
| `panda_eid.png` | Eid panda (crescent, lantern, dates) | Id-ul-Fitr, Id-ul-Alha |
| `panda_deepavali.png` | Deepavali panda (diyas, rangoli) | Deepavali |
| `panda_independence.png` | Independence Day panda (national dress, flag) | 4 Feb |
| `panda_newyear.png` *(optional)* | 1 Jan party panda | 1 Jan, if added later |

## Notes for the illustrator
- Same face, proportions, outline weight and palette as the logo; only clothing/props change.
- Respect religious and cultural dress and symbols; have a native reader check Vesak/Poya/Eid/Deepavali.
- Do not use the national flag lion, religious icons, or any third-party character as decoration without checking.
- Provide dark-background-friendly art (the UI is dark purple/gold); add a thin light outline if needed.
- Optional: 2–3 frames per character (blink / wave) for a light animation later.

## For the developer
Until a file exists the app keeps the vector costume (Santa hat, lantern, crescent, etc.), so files can be
added one at a time.
