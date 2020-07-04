import {Property} from "./property";
import {UserProposal} from "./userProposal";
import {User} from "./user";

export class Proposal {
  id: number;
  userProposals: UserProposal[];
  property: Property;
  state: ProposalState;
  creator: User;
}

export type ProposalState = 'PENDING' | 'SENT' | 'ACCEPTED' | 'DECLINED' | 'CANCELED' | 'DROPPED';
export const ProposalState = {
  Pending: 'PENDING' as ProposalState,
  Sent: 'SENT' as ProposalState,
  Accepted: 'ACCEPTED' as ProposalState,
  Declined: 'DECLINED' as ProposalState,
  Canceled: 'CANCELED' as ProposalState,
  Dropped: 'DROPPED' as ProposalState
}




