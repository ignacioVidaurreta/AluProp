import {Proposal} from "./proposal";
import {User} from "./user";

export class UserProposal {
  id: number;
  user: User;
  proposal: Proposal;
  state: UserProposalState;
}

export type UserProposalState = 'PENDING' | 'ACCEPTED' | 'REJECTED';
export const UserProposalState = {
  Pending: 'PENDING' as UserProposalState,
  Accepted: 'ACCEPTED' as UserProposalState,
  Rejected: 'REJECTED' as UserProposalState
};
