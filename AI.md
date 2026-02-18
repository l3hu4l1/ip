AI used: Copilot, IntelliJ

### What I used it for
**1) Bootstrapping & scaffolding**
- Letting AI generate the “boring but necessary” code

**2) Iterating on logic-heavy parts**
- Using AI as a “rubber duck with opinions” for complex logic. It helped surface scenarios I wouldn’t have thought of immediately.
- **What didn’t work:** If my prompt was vague, it would confidently invent behaviors and inadvertently change 
  requirements.

**3) Debugging**
- **What worked:** Giving AI an error message and asking for a list of likely causes.
- **What didn’t:** When the bug was _deeply contextual_ (config mismatch, environment-specific behavior, build 
  tooling nuances), AI suggestions became generic.
- Faster triage, but still required human judgment to pick the right hypothesis.

**4) Testing**
- Generate tests for scenarios with explicit list of behaviors

**5) Refactoring**
- **What worked:** Asking for refactors with specific constraints
- **What didn’t:** Large-scale refactors across many files without careful incremental checkpoints.
- Minimised my mental overhead, as long as changes were reviewed in small chunks.

### Interesting observations
- AI is better at breadth than depth.
- It will rarely say “I’m not sure” unless you force it to list assumptions and uncertainties.
- It can subtly change requirements.

### How much time did it save
Overall, about a few hours in a day on a medium-sized feature set. But I had to spend time reviewing its 
output and keeping changes more incremental.
