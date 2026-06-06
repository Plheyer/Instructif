const GRADES = { 6: '6ème', 5: '5ème', 4: '4ème', 3: '3ème', 2: '2nde', 1: '1ère', 0: 'Terminale' };

export function formatGrade(n) {
    return GRADES[n] ?? n + 'ème';
}
