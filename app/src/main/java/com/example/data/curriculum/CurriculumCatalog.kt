package com.example.data.curriculum

import com.example.data.model.BadgeDefinition
import com.example.data.model.BadgeMetric
import com.example.data.model.Lesson
import com.example.data.model.LevelInfo
import com.example.data.model.QuizQuestion
import com.example.data.model.StudyCard
import com.example.data.model.SubjectCategory

object CurriculumCatalog {

    val lessons: List<Lesson> = listOf(
        // --- MATH & LOGIC ---
        Lesson(
            id = "math_ratios",
            subject = SubjectCategory.MATH,
            order = 1,
            title = "Percentages & Ratios",
            subtitle = "Compare quantities fast using proportions and 100-part scaling",
            durationMinutes = 4,
            baseXp = 60,
            cards = listOf(
                StudyCard(
                    heading = "What a Ratio Tells You",
                    body = "A ratio compares two quantities directly, like 3 cups of water to 1 cup of rice (3:1). Scaling both sides by the same multiplier keeps the balance identical.",
                    keyTakeaway = "Ratio a:b equals (a × k) : (b × k) for any positive multiplier k.",
                    realWorldExample = "If a study group has 2 mentors for 10 students (1:5), 30 students need 6 mentors."
                ),
                StudyCard(
                    heading = "Percentages as Parts of 100",
                    body = "Percent literally means 'per hundred'. Converting a fraction to a percentage standardizes comparisons across completely different totals.",
                    keyTakeaway = "Percentage = (Part ÷ Whole) × 100%",
                    realWorldExample = "Scoring 18 out of 20 on a quiz is (18 ÷ 20) × 100 = 90%."
                ),
                StudyCard(
                    heading = "Mental Percentage Shortcuts",
                    body = "Find 10% by shifting the decimal left once, and 1% by shifting twice. Combine them to compute tips, discounts, and growth rates effortlessly.",
                    keyTakeaway = "15% of N = (10% of N) + half of (10% of N).",
                    realWorldExample = "20% off a ₹500 textbook: 10% is ₹50, so 20% is ₹100 off — paying ₹400."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "If you score 21 out of 25 on a science test, what is your percentage?",
                    options = listOf("76%", "84%", "88%", "92%"),
                    correctIndex = 1,
                    explanation = "21 ÷ 25 = 0.84, and 0.84 × 100 = 84%."
                ),
                QuizQuestion(
                    prompt = "A paint mix uses 2 parts blue to 3 parts yellow. How much yellow pairs with 8 parts blue?",
                    options = listOf("9 parts", "10 parts", "12 parts", "15 parts"),
                    correctIndex = 2,
                    explanation = "Blue multiplied by 4 (2 × 4 = 8), so yellow also multiplies by 4 (3 × 4 = 12)."
                ),
                QuizQuestion(
                    prompt = "A ₹800 jacket is discounted by 15%. What is the discount amount?",
                    options = listOf("₹80", "₹100", "₹120", "₹150"),
                    correctIndex = 2,
                    explanation = "10% of 800 is 80, and 5% is 40. Together, 80 + 40 = ₹120."
                )
            )
        ),
        Lesson(
            id = "math_geometry",
            subject = SubjectCategory.MATH,
            order = 2,
            title = "Geometry Foundations",
            subtitle = "Triangles, circles, and why right angles rule engineering",
            durationMinutes = 5,
            baseXp = 70,
            cards = listOf(
                StudyCard(
                    heading = "The Triangle Angle Law",
                    body = "No matter how stretched or tilted a triangle is on a flat plane, its three interior angles always add up to exactly 180 degrees.",
                    keyTakeaway = "Angle A + Angle B + Angle C = 180° in every Euclidean triangle.",
                    realWorldExample = "If a roof truss has two 55° base angles, the top peak angle must be 180 - 110 = 70°."
                ),
                StudyCard(
                    heading = "Pythagoras & Right Triangles",
                    body = "In any right-angled triangle, the square of the longest side (hypotenuse) equals the sum of the squares of the other two sides.",
                    keyTakeaway = "a² + b² = c² (famous triple: 3, 4, 5).",
                    realWorldExample = "Builders use a 3m-4m-5m rope triangle to verify corners are a true 90° square."
                ),
                StudyCard(
                    heading = "Circles and Pi (π)",
                    body = "Pi (~3.1416) is the universal ratio between any circle's circumference and its diameter, whether it's a coin or a planet.",
                    keyTakeaway = "Circumference = 2πr, Area = πr².",
                    realWorldExample = "A bicycle wheel with radius 35 cm travels about 2 × (22/7) × 35 = 220 cm per turn."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "A triangle has angles of 40° and 65°. What is the third angle?",
                    options = listOf("65°", "75°", "85°", "95°"),
                    correctIndex = 1,
                    explanation = "180° - (40° + 65°) = 180° - 105° = 75°."
                ),
                QuizQuestion(
                    prompt = "A right triangle has legs of length 6 and 8. How long is the hypotenuse?",
                    options = listOf("9", "10", "12", "14"),
                    correctIndex = 1,
                    explanation = "6² + 8² = 36 + 64 = 100, and √100 = 10."
                ),
                QuizQuestion(
                    prompt = "If a circle's radius doubles, what happens to its area?",
                    options = listOf("It doubles", "It triples", "It quadruples (4×)", "It stays the same"),
                    correctIndex = 2,
                    explanation = "Area = πr², so replacing r with 2r gives π(2r)² = 4πr², which is 4 times larger."
                )
            )
        ),
        Lesson(
            id = "math_probability",
            subject = SubjectCategory.MATH,
            order = 3,
            title = "Probability & Odds",
            subtitle = "Measure uncertainty and avoid common chance fallacies",
            durationMinutes = 4,
            baseXp = 65,
            cards = listOf(
                StudyCard(
                    heading = "Counting Favorable Outcomes",
                    body = "Probability measures how likely an event is on a scale from 0 (impossible) to 1 (certain) by dividing favorable outcomes by total equally likely outcomes.",
                    keyTakeaway = "P(Event) = Favorable Outcomes ÷ Total Outcomes.",
                    realWorldExample = "Rolling an even number (2, 4, 6) on a 6-sided die has probability 3/6 = 0.5."
                ),
                StudyCard(
                    heading = "Independent Events Multiply",
                    body = "When one event doesn't affect the next—like separate coin flips—you multiply their individual probabilities to find the combined chance.",
                    keyTakeaway = "P(A and B) = P(A) × P(B) for independent events.",
                    realWorldExample = "Flipping two heads in a row is (1/2) × (1/2) = 1/4, or 25%."
                ),
                StudyCard(
                    heading = "Beware the Gambler's Fallacy",
                    body = "Coins and dice have no memory. After five heads in a row, the probability of heads on the very next fair flip is still 50%.",
                    keyTakeaway = "Past independent trials never change future single-trial odds.",
                    realWorldExample = "Weather satellites model dependent trends, whereas fair lottery draws stay independent."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "A bag holds 3 red, 2 blue, and 5 green marbles. What is the chance of picking a blue marble?",
                    options = listOf("1/10", "1/5", "1/3", "2/5"),
                    correctIndex = 1,
                    explanation = "There are 2 blue marbles out of 10 total marbles: 2/10 = 1/5."
                ),
                QuizQuestion(
                    prompt = "What is the probability of rolling a 6 twice in a row on a standard 6-sided die?",
                    options = listOf("1/12", "1/18", "1/36", "2/6"),
                    correctIndex = 2,
                    explanation = "Each roll is independent: (1/6) × (1/6) = 1/36."
                ),
                QuizQuestion(
                    prompt = "A fair coin landed Heads 4 times in a row. What is the chance of Tails on the 5th flip?",
                    options = listOf("50%", "75%", "80%", "25%"),
                    correctIndex = 0,
                    explanation = "A fair coin has no memory, so every single flip always has a 50% chance of Tails."
                )
            )
        ),

        // --- SCIENCE ---
        Lesson(
            id = "sci_solar",
            subject = SubjectCategory.SCIENCE,
            order = 1,
            title = "Solar System & Gravity",
            subtitle = "How orbits stay balanced and why planets differ",
            durationMinutes = 4,
            baseXp = 60,
            cards = listOf(
                StudyCard(
                    heading = "Gravity & Forward Speed",
                    body = "Orbits happen because gravity pulls a planet inward while its sideways velocity keeps it continually 'falling around' the Sun without crashing in.",
                    keyTakeaway = "Stable orbit = inward gravitational pull + sideways orbital speed.",
                    realWorldExample = "The International Space Station travels at ~28,000 km/h to stay in orbit around Earth."
                ),
                StudyCard(
                    heading = "Rocky Inner vs Gas Giants",
                    body = "The four inner planets (Mercury, Venus, Earth, Mars) have solid rocky crusts. Beyond the asteroid belt, Jupiter and Saturn are huge gas giants.",
                    keyTakeaway = "Inner 4 = Terrestrial/Rocky; Outer 4 = Gas & Ice Giants.",
                    realWorldExample = "Jupiter is so massive that more than 1,300 Earths could fit inside its volume."
                ),
                StudyCard(
                    heading = "Light-Minutes & Scale",
                    body = "Space distances are measured using light speed (~300,000 km/s). Sunlight takes roughly 8 minutes and 20 seconds to reach Earth.",
                    keyTakeaway = "1 Astronomical Unit (AU) = average Earth-Sun distance (~150 million km).",
                    realWorldExample = "Mars takes 3 to 22 minutes for radio signals to reach Earth depending on orbital positions."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "Why doesn't the Moon crash straight into Earth under gravity?",
                    options = listOf(
                        "There is no gravity in space",
                        "Its sideways orbital speed balances Earth's pull",
                        "Solar wind pushes it outward",
                        "It is lighter than air"
                    ),
                    correctIndex = 1,
                    explanation = "Gravity does pull the Moon, but its tangential orbital motion keeps it continuously curving around Earth."
                ),
                QuizQuestion(
                    prompt = "Which of these is a rocky terrestrial planet?",
                    options = listOf("Saturn", "Neptune", "Mars", "Uranus"),
                    correctIndex = 2,
                    explanation = "Mars is one of the four inner rocky planets alongside Mercury, Venus, and Earth."
                ),
                QuizQuestion(
                    prompt = "Approximately how long does light from the Sun take to reach Earth?",
                    options = listOf("8 seconds", "8 minutes", "8 hours", "8 days"),
                    correctIndex = 1,
                    explanation = "Sunlight crosses the 150 million km gap at 300,000 km/s in about 8 minutes 20 seconds."
                )
            )
        ),
        Lesson(
            id = "sci_matter",
            subject = SubjectCategory.SCIENCE,
            order = 2,
            title = "States of Matter & Energy",
            subtitle = "Explore particle motion from solid ice to ionized plasma",
            durationMinutes = 4,
            baseXp = 65,
            cards = listOf(
                StudyCard(
                    heading = "Kinetic Energy of Particles",
                    body = "All matter is made of tiny moving particles. Adding thermal energy makes particles vibrate and slide faster, driving phase changes.",
                    keyTakeaway = "Temperature measures the average kinetic energy of particles.",
                    realWorldExample = "Heating solid ice gives water molecules enough energy to break rigid bonds and melt."
                ),
                StudyCard(
                    heading = "Solids, Liquids, Gases",
                    body = "Solids keep fixed shape and volume. Liquids flow to match a container's shape while keeping volume. Gases expand to fill any space.",
                    keyTakeaway = "Particle spacing increases: Solid < Liquid << Gas.",
                    realWorldExample = "Perfume sprayed in one corner spreads across the entire room via gas diffusion."
                ),
                StudyCard(
                    heading = "Plasma: The Fourth State",
                    body = "At extreme heat or electrical voltage, electrons strip away from atoms, creating glowing electrically charged plasma.",
                    keyTakeaway = "Over 99% of visible matter in the universe (stars & nebulae) is plasma.",
                    realWorldExample = "Lightning bolts, auroras, and neon signs are everyday examples of plasma on Earth."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "Which state of matter has a fixed volume but changes shape to fit its container?",
                    options = listOf("Solid", "Liquid", "Gas", "Plasma"),
                    correctIndex = 1,
                    explanation = "Liquids maintain near-constant volume while their particles slide past one another to take the container's shape."
                ),
                QuizQuestion(
                    prompt = "What happens to particle motion when a substance is cooled down?",
                    options = listOf(
                        "Particles speed up",
                        "Particles slow down",
                        "Particles vanish",
                        "Particles gain mass"
                    ),
                    correctIndex = 1,
                    explanation = "Cooling removes thermal kinetic energy, causing particles to move and vibrate more slowly."
                ),
                QuizQuestion(
                    prompt = "What state of matter makes up the Sun and lightning bolts?",
                    options = listOf("Superheated liquid", "Dense solid", "Plasma", "Dry gas"),
                    correctIndex = 2,
                    explanation = "Stars and lightning ionize gas into electrically charged plasma."
                )
            )
        ),
        Lesson(
            id = "sci_energy",
            subject = SubjectCategory.SCIENCE,
            order = 3,
            title = "Clean & Renewable Energy",
            subtitle = "How solar panels, wind turbines, and batteries power the future",
            durationMinutes = 5,
            baseXp = 75,
            cards = listOf(
                StudyCard(
                    heading = "Photovoltaic Solar Cells",
                    body = "Solar panels use silicon semiconductors. When sunlight photons strike the silicon, they knock electrons loose to create direct electrical current.",
                    keyTakeaway = "Solar PV converts light energy directly into electricity with no moving parts.",
                    realWorldExample = "Rooftop solar arrays combined with inverters power homes and feed excess power to the grid."
                ),
                StudyCard(
                    heading = "Wind & Hydroelectric Generators",
                    body = "Moving air (wind) or falling water spins turbine blades connected to magnets inside copper coils, generating electricity via electromagnetic induction.",
                    keyTakeaway = "Kinetic motion → spinning magnet → electric current (Faraday's Law).",
                    realWorldExample = "Offshore wind turbines capture steadier ocean winds to power coastal cities."
                ),
                StudyCard(
                    heading = "Grid Storage & Batteries",
                    body = "Since sunshine and wind vary by hour, lithium-ion batteries and pumped-hydro reservoirs store surplus clean energy for calm nights.",
                    keyTakeaway = "Energy storage bridges supply peaks and evening demand.",
                    realWorldExample = "Pumped hydro pumps water uphill at noon and releases it through turbines at night."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "How do photovoltaic solar panels produce electricity?",
                    options = listOf(
                        "By boiling water with mirrors",
                        "Photons knock electrons loose in silicon",
                        "Spinning internal wind fans",
                        "Burning hydrogen gas"
                    ),
                    correctIndex = 1,
                    explanation = "Photovoltaic cells use the photoelectric effect in silicon semiconductors to turn light directly into DC electricity."
                ),
                QuizQuestion(
                    prompt = "Which physics principle allows wind turbines to generate electricity from spinning magnets?",
                    options = listOf("Electromagnetic induction", "Nuclear fission", "Osmosis", "Refraction"),
                    correctIndex = 0,
                    explanation = "Changing magnetic fields inside wire coils induce electric current—Faraday's law of electromagnetic induction."
                ),
                QuizQuestion(
                    prompt = "Why is energy storage critical for renewable power grids?",
                    options = listOf(
                        "It paints turbines white",
                        "It balances supply when sun or wind dips",
                        "It increases fossil fuel use",
                        "It slows down electrons"
                    ),
                    correctIndex = 1,
                    explanation = "Storage captures surplus daytime/windy energy and dispatches it during peak evening hours."
                )
            )
        ),

        // --- COMPUTER SCIENCE ---
        Lesson(
            id = "cs_binary",
            subject = SubjectCategory.CODING,
            order = 1,
            title = "Binary & Digital Bits",
            subtitle = "Understand how 0s and 1s represent numbers, text, and images",
            durationMinutes = 4,
            baseXp = 60,
            cards = listOf(
                StudyCard(
                    heading = "Why Computers Use Base-2",
                    body = "Inside a processor, billions of tiny transistors act like fast switches that are either OFF (0) or ON (1). Each switch holds one 'bit'.",
                    keyTakeaway = "1 Byte = 8 bits, capable of representing 256 distinct values (0–255).",
                    realWorldExample = "Every photo, song, and app on your phone is stored as structured sequences of bits."
                ),
                StudyCard(
                    heading = "Reading Binary Numbers",
                    body = "Instead of powers of 10 (1, 10, 100), binary places represent powers of 2 from right to left: 1, 2, 4, 8, 16...",
                    keyTakeaway = "Binary 1011 = (1×8) + (0×4) + (1×2) + (1×1) = 11 in decimal.",
                    realWorldExample = "IP addresses and file permissions use binary bitmasks under the hood."
                ),
                StudyCard(
                    heading = "Encoding Color & Text",
                    body = "Standards like UTF-8 map letters to numbers, while RGB screens mix 1 byte each of Red, Green, and Blue per pixel.",
                    keyTakeaway = "24-bit RGB color creates over 16.7 million shades per pixel.",
                    realWorldExample = "The letter 'A' is ASCII/UTF-8 code 65, which is 01000001 in binary."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "How many bits make up one Byte?",
                    options = listOf("4 bits", "8 bits", "16 bits", "32 bits"),
                    correctIndex = 1,
                    explanation = "One byte consists of 8 bits, giving 2⁸ = 256 possible combinations."
                ),
                QuizQuestion(
                    prompt = "What decimal number does the binary value 1010 represent?",
                    options = listOf("6", "8", "10", "12"),
                    correctIndex = 2,
                    explanation = "1010 in binary = (1×8) + (0×4) + (1×2) + (0×1) = 8 + 2 = 10."
                ),
                QuizQuestion(
                    prompt = "How many combinations can 3 bits store?",
                    options = listOf("3", "6", "8", "9"),
                    correctIndex = 2,
                    explanation = "With 3 bits, there are 2³ = 8 distinct patterns (000 through 111)."
                )
            )
        ),
        Lesson(
            id = "cs_algorithms",
            subject = SubjectCategory.CODING,
            order = 2,
            title = "Algorithms & Big-O Speed",
            subtitle = "Compare linear search with blazing-fast binary search",
            durationMinutes = 5,
            baseXp = 75,
            cards = listOf(
                StudyCard(
                    heading = "What Makes an Algorithm",
                    body = "An algorithm is a clear, finite recipe of steps that turns inputs into a guaranteed output—just like navigation directions.",
                    keyTakeaway = "Great algorithms are both correct and efficient as data grows.",
                    realWorldExample = "GPS apps run shortest-path graph algorithms to route around live traffic."
                ),
                StudyCard(
                    heading = "Linear vs Binary Search",
                    body = "Checking items one by one takes up to N steps (O(N)). On a sorted list, binary search checks the middle and cuts the search space in half each step (O(log N)).",
                    keyTakeaway = "Searching 1,000,000 sorted names takes ~20 steps with binary search vs 1,000,000 linearly!",
                    realWorldExample = "Looking up a word in a paper dictionary by opening near the middle is binary search."
                ),
                StudyCard(
                    heading = "Sorting Unlocks Speed",
                    body = "Algorithms like Merge Sort organize N items in O(N log N) time by recursively splitting and merging sorted halves.",
                    keyTakeaway = "Divide-and-conquer breaks huge problems into small solvable pieces.",
                    realWorldExample = "Leaderboards sort players by XP score so top ranks appear instantly."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "What requirement must a list satisfy before you can run Binary Search on it?",
                    options = listOf(
                        "It must contain only numbers",
                        "It must be sorted",
                        "It must have fewer than 100 items",
                        "It must have no duplicates"
                    ),
                    correctIndex = 1,
                    explanation = "Binary search decides whether to go left or right by comparing against the midpoint, which only works on sorted data."
                ),
                QuizQuestion(
                    prompt = "At most how many comparisons does Binary Search need on 16 sorted items?",
                    options = listOf("2", "4", "8", "16"),
                    correctIndex = 1,
                    explanation = "Each check halves the search space: 16 → 8 → 4 → 2 → 1, so at most 4 steps (log₂ 16 = 4)."
                ),
                QuizQuestion(
                    prompt = "Which Big-O notation describes checking every element once in a single loop?",
                    options = listOf("O(1)", "O(log N)", "O(N)", "O(N²)"),
                    correctIndex = 2,
                    explanation = "A single pass over N elements scales linearly with input size, written O(N)."
                )
            )
        ),
        Lesson(
            id = "cs_security",
            subject = SubjectCategory.CODING,
            order = 3,
            title = "Cybersecurity & Encryption",
            subtitle = "Protect passwords, spot phishing, and understand public keys",
            durationMinutes = 4,
            baseXp = 70,
            cards = listOf(
                StudyCard(
                    heading = "Hashing vs Encryption",
                    body = "Encryption scrambles data so it can be unlocked with a key. Hashing is a one-way mathematical fingerprint used to store passwords safely.",
                    keyTakeaway = "Encryption is two-way (reversible); cryptographic hashing is one-way.",
                    realWorldExample = "Websites hash your password with salt so even engineers cannot read your plain password."
                ),
                StudyCard(
                    heading = "Public-Key HTTPS Lock",
                    body = "When you see HTTPS, your browser uses asymmetric keys to securely agree on a shared secret session key over an open network.",
                    keyTakeaway = "Public keys lock messages; only the matching private key unlocks them.",
                    realWorldExample = "Banking apps encrypt every card detail using TLS before packets leave your phone."
                ),
                StudyCard(
                    heading = "Two-Factor & Phishing Defense",
                    body = "Strong unique passwords paired with 2FA (passkeys or authenticator codes) stop over 99% of automated credential attacks.",
                    keyTakeaway = "Always verify the exact domain URL before entering any login credentials.",
                    realWorldExample = "Phishing links often swap letters like 'rn' for 'm' in fake login emails."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "Why do secure services store password hashes instead of encrypted passwords?",
                    options = listOf(
                        "Hashes are shorter to type",
                        "Hashes are one-way and cannot be reversed to plain text",
                        "Hashes work without internet",
                        "Hashes make passwords easier to guess"
                    ),
                    correctIndex = 1,
                    explanation = "One-way cryptographic hashes prevent attackers from recovering original passwords even if a database leaks."
                ),
                QuizQuestion(
                    prompt = "In public-key cryptography, which key should NEVER be shared?",
                    options = listOf("Public key", "Private key", "Website URL", "HTTPS certificate"),
                    correctIndex = 1,
                    explanation = "Anyone can use your public key to encrypt data for you, but only your private key must remain secret to decrypt it."
                ),
                QuizQuestion(
                    prompt = "Which habit gives the strongest boost against stolen passwords?",
                    options = listOf(
                        "Reusing one complex password everywhere",
                        "Enabling Two-Factor Authentication (2FA)",
                        "Writing passwords in public notes",
                        "Using your birth year"
                    ),
                    correctIndex = 1,
                    explanation = "2FA requires a second verification factor beyond the password, blocking unauthorized logins."
                )
            )
        ),

        // --- HISTORY & CIVICS ---
        Lesson(
            id = "hist_ancient",
            subject = SubjectCategory.HISTORY,
            order = 1,
            title = "Indus Valley & Ancient Cities",
            subtitle = "Discover grid urban planning, drainage, and early trade",
            durationMinutes = 4,
            baseXp = 60,
            cards = listOf(
                StudyCard(
                    heading = "Master Urban Planners",
                    body = "Around 2600 BCE, cities like Mohenjo-daro and Harappa featured brick streets laid in precise rectangular grids and standardized baked bricks.",
                    keyTakeaway = "Harappan cities pioneered covered brick drains and public granaries 4,500 years ago.",
                    realWorldExample = "Homes connected directly to street-level wastewater drains with inspection covers."
                ),
                StudyCard(
                    heading = "Weights, Seals & Trade",
                    body = "Merchants used standardized stone weights in binary and decimal ratios and carved steatite seals to stamp goods traded as far as Mesopotamia.",
                    keyTakeaway = "Standardized measurement enabled fair long-distance commerce.",
                    realWorldExample = "Harappan carnelian beads have been unearthed in ancient tombs in Iraq."
                ),
                StudyCard(
                    heading = "Water Engineering at Dholavira",
                    body = "In arid Gujarat, Dholavira engineers built interconnected rock-cut reservoirs to harvest seasonal monsoon rainwater year-round.",
                    keyTakeaway = "Ancient water conservation sustained thriving cities in dry climates.",
                    realWorldExample = "Dholavira is a UNESCO World Heritage site showcasing ancient stepwell geometry."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "What stood out as a hallmark engineering feature of Indus Valley cities?",
                    options = listOf(
                        "Iron skyscrapers",
                        "Grid streets with covered drainage systems",
                        "Steam locomotives",
                        "Glass pyramids"
                    ),
                    correctIndex = 1,
                    explanation = "Mohenjo-daro and Harappa were renowned for grid-planned streets, standardized bricks, and covered sanitation drains."
                ),
                QuizQuestion(
                    prompt = "How did Dholavira thrive despite an arid climate?",
                    options = listOf(
                        "Snowmelt pipelines",
                        "Interconnected rainwater harvesting reservoirs",
                        "Ocean desalination plants",
                        "Underground rivers"
                    ),
                    correctIndex = 1,
                    explanation = "Engineers in Dholavira carved massive stone reservoirs to capture and store monsoon water."
                ),
                QuizQuestion(
                    prompt = "What were carved Harappan steatite seals primarily used for?",
                    options = listOf(
                        "Fueling ovens",
                        "Stamping trade goods and identifying merchants",
                        "Writing musical scores",
                        "Building roofs"
                    ),
                    correctIndex = 1,
                    explanation = "Merchants pressed engraved seals into clay tags to mark ownership and authenticate trade bundles."
                )
            )
        ),
        Lesson(
            id = "hist_science_rev",
            subject = SubjectCategory.HISTORY,
            order = 2,
            title = "The Scientific Method Pioneers",
            subtitle = "From Aryabhata's astronomy to Galileo's telescope",
            durationMinutes = 4,
            baseXp = 65,
            cards = listOf(
                StudyCard(
                    heading = "Aryabhata & Zero's Power",
                    body = "In 499 CE, Indian mathematician-astronomer Aryabhata used place-value notation, estimated Pi accurately, and explained solar eclipses as shadows rather than myths.",
                    keyTakeaway = "Place-value arithmetic with zero transformed global mathematics and science.",
                    realWorldExample = "India's first satellite launched in 1975 was named 'Aryabhata' in his honor."
                ),
                StudyCard(
                    heading = "Observation Over Assumption",
                    body = "The Scientific Revolution championed testing ideas through repeatable experiments and careful measurement rather than accepting old claims blindly.",
                    keyTakeaway = "Hypothesis → Controlled Experiment → Evidence → Peer Review.",
                    realWorldExample = "Galileo's observations of Jupiter's moons proved not everything orbited Earth."
                ),
                StudyCard(
                    heading = "Printing Press & Shared Knowledge",
                    body = "Affordable printing spread discoveries across continents, letting scholars verify and build on each other's equations rapidly.",
                    keyTakeaway = "Open sharing accelerates human discovery exponentially.",
                    realWorldExample = "Modern open-access journals carry forward that same collaborative spirit."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "How did Aryabhata explain solar and lunar eclipses?",
                    options = listOf(
                        "Ocean tides shifting",
                        "Shadows cast by Earth and the Moon",
                        "Comets blocking stars",
                        "Solar flares cooling"
                    ),
                    correctIndex = 1,
                    explanation = "Aryabhata accurately modeled eclipses as geometric shadows cast by Earth and the Moon."
                ),
                QuizQuestion(
                    prompt = "What is the core principle of the Scientific Method?",
                    options = listOf(
                        "Trusting authority without testing",
                        "Testing hypotheses with repeatable evidence",
                        "Keeping discoveries secret",
                        "Guessing once"
                    ),
                    correctIndex = 1,
                    explanation = "Science relies on testable predictions verified through repeatable experiments and data."
                ),
                QuizQuestion(
                    prompt = "What did Galileo discover by pointing his telescope at Jupiter?",
                    options = listOf(
                        "Rings around Mars",
                        "Moons orbiting Jupiter",
                        "Pluto's atmosphere",
                        "Radio waves"
                    ),
                    correctIndex = 1,
                    explanation = "Observing four moons orbiting Jupiter proved celestial bodies could orbit worlds other than Earth."
                )
            )
        ),
        Lesson(
            id = "hist_civics",
            subject = SubjectCategory.HISTORY,
            order = 3,
            title = "Constitutional Rights & Civics",
            subtitle = "How democratic institutions balance rights and duties",
            durationMinutes = 5,
            baseXp = 70,
            cards = listOf(
                StudyCard(
                    heading = "Separation of Powers",
                    body = "Healthy democracies divide power among three branches: Legislature (makes laws), Executive (enforces laws), and Judiciary (interprets laws fairly).",
                    keyTakeaway = "Checks and balances prevent any single branch from holding unchecked power.",
                    realWorldExample = "Independent courts can strike down laws that violate fundamental constitutional rights."
                ),
                StudyCard(
                    heading = "Fundamental Rights & Education",
                    body = "Modern constitutions safeguard equality, free expression, and the Right to Education so every child can learn regardless of background.",
                    keyTakeaway = "Article 21A of India's Constitution guarantees free, compulsory education for ages 6–14.",
                    realWorldExample = "Public libraries and scholarship programs put constitutional equality into practice."
                ),
                StudyCard(
                    heading = "Active Civic Responsibility",
                    body = "Rights thrive when citizens pair them with civic duties—voting thoughtfully, protecting public property, and respecting scientific temper.",
                    keyTakeaway = "An informed learner is the strongest foundation of any democracy.",
                    realWorldExample = "Local youth councils and clean-neighborhood drives show active citizenship in action."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    prompt = "Which branch of government interprets laws and protects constitutional rights?",
                    options = listOf("Legislature", "Executive", "Judiciary", "Treasury"),
                    correctIndex = 2,
                    explanation = "The Judiciary independently interprets laws and reviews whether actions respect the Constitution."
                ),
                QuizQuestion(
                    prompt = "Why do democracies separate Legislative, Executive, and Judicial powers?",
                    options = listOf(
                        "To create checks and balances",
                        "To stop elections",
                        "To eliminate courts",
                        "To slow down schools"
                    ),
                    correctIndex = 0,
                    explanation = "Separating powers ensures each branch can check abuses and protect citizens' liberties."
                ),
                QuizQuestion(
                    prompt = "Which constitutional guarantee directly empowers young learners in India (Article 21A)?",
                    options = listOf(
                        "Right to Free & Compulsory Education",
                        "Right to Drive at Age 10",
                        "Right to Tax Exemption",
                        "Right to Skip Exams"
                    ),
                    correctIndex = 0,
                    explanation = "Article 21A recognizes education as a fundamental right for children aged 6 to 14."
                )
            )
        )
    )

    val badges: List<BadgeDefinition> = listOf(
        BadgeDefinition(
            id = "badge_first_step",
            title = "First Spark",
            description = "Complete your very first lesson and quiz",
            metric = BadgeMetric.LESSONS_COMPLETED,
            targetValue = 1,
            rewardXp = 30,
            tierLabel = "Bronze"
        ),
        BadgeDefinition(
            id = "badge_sharp_mind",
            title = "Sharp Mind",
            description = "Score 100% accuracy on any lesson quiz",
            metric = BadgeMetric.PERFECT_QUIZZES,
            targetValue = 1,
            rewardXp = 40,
            tierLabel = "Bronze"
        ),
        BadgeDefinition(
            id = "badge_hat_trick",
            title = "Hat Trick",
            description = "Complete 3 lessons across your learning journey",
            metric = BadgeMetric.LESSONS_COMPLETED,
            targetValue = 3,
            rewardXp = 50,
            tierLabel = "Silver"
        ),
        BadgeDefinition(
            id = "badge_polymath",
            title = "Curious Polymath",
            description = "Complete at least 1 lesson in 3 different subjects",
            metric = BadgeMetric.SUBJECTS_EXPLORED,
            targetValue = 3,
            rewardXp = 60,
            tierLabel = "Silver"
        ),
        BadgeDefinition(
            id = "badge_xp_hunter",
            title = "XP Trailblazer",
            description = "Accumulate 350 total XP points",
            metric = BadgeMetric.TOTAL_XP,
            targetValue = 350,
            rewardXp = 50,
            tierLabel = "Silver"
        ),
        BadgeDefinition(
            id = "badge_track_master",
            title = "Track Master",
            description = "Finish all lessons in any single subject track",
            metric = BadgeMetric.TRACK_MASTERED,
            targetValue = 1,
            rewardXp = 90,
            tierLabel = "Gold"
        ),
        BadgeDefinition(
            id = "badge_quiz_ace",
            title = "Quiz Perfectionist",
            description = "Achieve 100% accuracy on 5 different lesson quizzes",
            metric = BadgeMetric.PERFECT_QUIZZES,
            targetValue = 5,
            rewardXp = 100,
            tierLabel = "Gold"
        ),
        BadgeDefinition(
            id = "badge_grand_savant",
            title = "Grand Savant",
            description = "Reach 1,000 total XP and inspire the leaderboard",
            metric = BadgeMetric.TOTAL_XP,
            targetValue = 1000,
            rewardXp = 150,
            tierLabel = "Platinum"
        )
    )

    fun lessonById(id: String): Lesson? = lessons.find { it.id == id }

    fun lessonsForSubject(subject: SubjectCategory): List<Lesson> =
        lessons.filter { it.subject == subject }.sortedBy { it.order }

    fun computeLevel(totalXp: Int): LevelInfo {
        val thresholds = listOf(0, 150, 350, 650, 1000, 1500)
        val titles = listOf(
            "Curious Explorer",
            "Knowledge Seeker",
            "Rising Scholar",
            "Subject Ace",
            "Master Thinker",
            "Grand Savant"
        )
        var currentLevel = 1
        for (i in 1 until thresholds.size) {
            if (totalXp >= thresholds[i]) {
                currentLevel = i + 1
            }
        }
        val floorXp = thresholds[(currentLevel - 1).coerceAtMost(thresholds.lastIndex)]
        val nextXp = thresholds.getOrNull(currentLevel) ?: (floorXp + 500)
        return LevelInfo(
            level = currentLevel,
            title = titles[(currentLevel - 1).coerceAtMost(titles.lastIndex)],
            currentXpInLevel = (totalXp - floorXp).coerceAtLeast(0),
            xpNeededForNextLevel = (nextXp - floorXp).coerceAtLeast(1)
        )
    }
}
