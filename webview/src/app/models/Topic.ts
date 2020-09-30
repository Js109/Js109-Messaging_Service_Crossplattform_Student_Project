export interface Topic {
  id: number;
  binding: string;
  title: string;
  tags: string[];
  description: string;
  disabled?: boolean;
}
