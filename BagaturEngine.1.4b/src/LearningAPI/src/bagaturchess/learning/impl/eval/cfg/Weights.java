package bagaturchess.learning.impl.eval.cfg;


public interface Weights {
	public static final double MATERIAL_PAWN_O		=	88.53199920069409;
	public static final double MATERIAL_PAWN_E		=	110.02070622910514;
	public static final double MATERIAL_KNIGHT_O	=	430.49111046321144;
	public static final double MATERIAL_KNIGHT_E	=	391.9227332285586;
	public static final double MATERIAL_BISHOP_O	=	443.634581290182;
	public static final double MATERIAL_BISHOP_E	=	425.5057547760457;
	public static final double MATERIAL_ROOK_O		=	616.3349023784737;
	public static final double MATERIAL_ROOK_E		=	685.5644647191372;
	public static final double MATERIAL_QUEEN_O		=	1341.269951331634;
	public static final double MATERIAL_QUEEN_E		=	1250.323241281083;
	public static final double KINGSAFE_CASTLING_O	=	8.695473272912482;
	public static final double KINGSAFE_CASTLING_E	=	0.0;
	public static final double KINGSAFE_FIANCHETTO_O=	0.030954314649276685;
	public static final double KINGSAFE_FIANCHETTO_E=	0.0;
	public static final double BISHOPS_DOUBLE_O		=	38.55304785134276;
	public static final double BISHOPS_DOUBLE_E		=	58.2527533291309;
	public static final double KNIGHTS_DOUBLE_O		=	-0.8418948768316152;
	public static final double KNIGHTS_DOUBLE_E		=	-0.3541363041112693;
	public static final double ROOKS_DOUBLE_O		=	-0.6394803203763532;
	public static final double ROOKS_DOUBLE_E		=	-0.9391348699444786;
	public static final double PAWNS5_ROOKS_O		=	-0.5390982511872409;
	public static final double PAWNS5_ROOKS_E		=	-1.3387240561386002;
	public static final double PAWNS5_KNIGHTS_O		=	3.699203461185335;
	public static final double PAWNS5_KNIGHTS_E		=	7.4901092619848955;
	public static final double KINGSAFE_F_O			=	-6.135834830407696;
	public static final double KINGSAFE_F_E			=	0.0;
	public static final double KINGSAFE_G_O			=	-0.031287654466305076;
	public static final double KINGSAFE_G_E			=	0.0;
	public static final double KINGS_DISTANCE_O		=	0;
	public static final double KINGS_DISTANCE_E		=	0.3593091534579083;
	public static final double PAWNS_DOUBLED_O		=	-0.06344121207625283;
	public static final double PAWNS_DOUBLED_E		=	-14.29780586088556;
	public static final double PAWNS_ISOLATED_O		=	-14.081104147241549;
	public static final double PAWNS_ISOLATED_E		=	-11.146861253903177;
	public static final double PAWNS_BACKWARD_O		=	-6.375578895582516;
	public static final double PAWNS_BACKWARD_E		=	-2.2179199051395857;
	public static final double PAWNS_SUPPORTED_O	=	3.609065058083699;
	public static final double PAWNS_SUPPORTED_E	=	3.3465159357916567;
	public static final double PAWNS_CANNOTBS_O		=	-1.6863498502616525;
	public static final double PAWNS_CANNOTBS_E		=	-0.5308967547913821;
	public static final double PAWNS_PASSED_O		=	4.680477377823983;
	public static final double PAWNS_PASSED_E		=	3.4342682149594577;
	public static final double PAWNS_PASSED_RNK_O	=	1.1424944013732647;
	public static final double PAWNS_PASSED_RNK_E	=	1.4706529633174854;
	public static final double PAWNS_UNSTOPPABLE_PASSER_O	=	0.0;
	public static final double PAWNS_UNSTOPPABLE_PASSER_E	=	550.0;
	public static final double PAWNS_CANDIDATE_RNK_O	=	1.9256140108109243;
	public static final double PAWNS_CANDIDATE_RNK_E	=	1.3898951461659623;
	public static final double KINGS_PASSERS_F_O	=	0.0;
	public static final double KINGS_PASSERS_F_E	=	1.771641359692092;
	public static final double KINGS_PASSERS_FF_O	=	0.0;
	public static final double KINGS_PASSERS_FF_E	=	1.0573809123088491;
	public static final double KINGS_PASSERS_F_OP_O	=	0.0;
	public static final double KINGS_PASSERS_F_OP_E	=	1.664989299655242;
	public static final double PAWNS_ISLANDS_O	=	-1.1426560677936877;
	public static final double PAWNS_ISLANDS_E	=	-0.6574727778250118;
	public static final double PAWNS_GARDS_O	=	15.095304223518145;
	public static final double PAWNS_GARDS_E	=	0.0;
	public static final double PAWNS_GARDS_REM_O	=	-6.817067617714686;
	public static final double PAWNS_GARDS_REM_E	=	0.0;
	public static final double PAWNS_STORMS_O	=	3.6562511086470018;
	public static final double PAWNS_STORMS_E	=	0.0;
	public static final double PAWNS_STORMS_CLS_O	=	1.7528654000457136;
	public static final double PAWNS_STORMS_CLS_E	=	0.0;
	public static final double PAWNS_OPENNED_O	=	-36.51630475211046;
	public static final double PAWNS_OPENNED_E	=	0.0;
	public static final double PAWNS_SEMIOP_OWN_O	=	-27.61263467545501;
	public static final double PAWNS_SEMIOP_OWN_E	=	0.0;
	public static final double PAWNS_SEMIOP_OP_O	=	-11.243766767056155;
	public static final double PAWNS_SEMIOP_OP_E	=	0.0;
	public static final double PAWNS_WEAK_O	=	-1.56049594965303;
	public static final double PAWNS_WEAK_E	=	-0.11469942441058452;
	public static final double SPACE_O	=	0.4842269012003156;
	public static final double SPACE_E	=	1.06495242638572;
	public static final double ROOK_INFRONT_PASSER_O	=	-26.281968971663698;
	public static final double ROOK_INFRONT_PASSER_E	=	-0.7874043073770787;
	public static final double ROOK_BEHIND_PASSER_O	=	0.16400430403572036;
	public static final double ROOK_BEHIND_PASSER_E	=	0.370505576804776;
	public static final double PST_PAWN_O	=	0.5710827986385989;
	public static final double PST_PAWN_E	=	0.9927040298136856;
	public static final double PST_KING_O	=	1.4152868740093765;
	public static final double PST_KING_E	=	1.6886842944327374;
	public static final double PST_KNIGHTS_O	=	1.020352936358533;
	public static final double PST_KNIGHTS_E	=	1.2065438212857906;
	public static final double PST_BISHOPS_O	=	1.1449938687083554;
	public static final double PST_BISHOPS_E	=	1.587546500773107;
	public static final double PST_ROOKS_O	=	1.862971446158503;
	public static final double PST_ROOKS_E	=	0.7278444261035139;
	public static final double PST_QUEENS_O	=	0.10831914054754384;
	public static final double PST_QUEENS_E	=	0.8606995592185696;
	public static final double BISHOPS_BAD_O	=	-0.47203138436696224;
	public static final double BISHOPS_BAD_E	=	-0.6447870737777084;
	public static final double KNIGHT_OUTPOST_O	=	6.189159441831243;
	public static final double KNIGHT_OUTPOST_E	=	1.4890542689350073;
	public static final double ROOKS_OPENED_O	=	24.64660545935084;
	public static final double ROOKS_OPENED_E	=	2.9523862741844824;
	public static final double ROOKS_SEMIOPENED_O	=	16.68542672912037;
	public static final double ROOKS_SEMIOPENED_E	=	1.0138229289325038;
	public static final double TROPISM_KNIGHT_O	=	0.09900906859684694;
	public static final double TROPISM_KNIGHT_E	=	0.0;
	public static final double TROPISM_BISHOP_O	=	0.4844399390677248;
	public static final double TROPISM_BISHOP_E	=	0.0;
	public static final double TROPISM_ROOK_O	=	0.6719263432348239;
	public static final double TROPISM_ROOK_E	=	0.0;
	public static final double TROPISM_QUEEN_O	=	0.12101756508375879;
	public static final double TROPISM_QUEEN_E	=	0.0;
	public static final double ROOKS_7TH_2TH_O	=	14.098363515907408;
	public static final double ROOKS_7TH_2TH_E	=	25.959258182976324;
	public static final double QUEENS_7TH_2TH_O	=	6.3427421276523095;
	public static final double QUEENS_7TH_2TH_E	=	9.95734336974601;
	public static final double KINGSAFETY_L1_O	=	50.79324915525265;
	public static final double KINGSAFETY_L1_E	=	0.0;
	public static final double KINGSAFETY_L2_O	=	7.139658663877436;
	public static final double KINGSAFETY_L2_E	=	0.0;
	public static final double MOBILITY_KNIGHT_O	=	0.799920552085603;
	public static final double MOBILITY_KNIGHT_E	=	1.4023260453044764;
	public static final double MOBILITY_BISHOP_O	=	0.7140264083199767;
	public static final double MOBILITY_BISHOP_E	=	1.1880883496711263;
	public static final double MOBILITY_ROOK_O	=	0.8519327926189777;
	public static final double MOBILITY_ROOK_E	=	1.3656293704586049;
	public static final double MOBILITY_QUEEN_O	=	0.038489031564274016;
	public static final double MOBILITY_QUEEN_E	=	0.7969717663469346;
	public static final double MOBILITY_KNIGHT_S_O	=	0.5701417732919651;
	public static final double MOBILITY_KNIGHT_S_E	=	1.5943119808282296;
	public static final double MOBILITY_BISHOP_S_O	=	0.5236869290177378;
	public static final double MOBILITY_BISHOP_S_E	=	0.702074640076866;
	public static final double MOBILITY_ROOK_S_O	=	0.4436233529865903;
	public static final double MOBILITY_ROOK_S_E	=	1.6241039419399184;
	public static final double MOBILITY_QUEEN_S_O	=	0.08976717176007516;
	public static final double MOBILITY_QUEEN_S_E	=	0.7694076178835493;
	public static final double PENETRATION_OP_O	=	0.17824204653201886;
	public static final double PENETRATION_OP_E	=	0.0;
	public static final double PENETRATION_OP_S_O	=	0.22735909034148527;
	public static final double PENETRATION_OP_S_E	=	0.0;
	public static final double PENETRATION_KING_O	=	0.1379869316404612;
	public static final double PENETRATION_KING_E	=	0.0;
	public static final double PENETRATION_KING_S_O	=	0.13623177607415432;
	public static final double PENETRATION_KING_S_E	=	0.0;
	public static final double ROOKS_PAIR_H_O	=	9.141756627196248;
	public static final double ROOKS_PAIR_H_E	=	1.7208904465692263;
	public static final double ROOKS_PAIR_V_O	=	0.20986129053952293;
	public static final double ROOKS_PAIR_V_E	=	0.49629572742464223;
	
	public static final double TRAP_O	=	2.3667991106010713;
	public static final double TRAP_E	=	0.4438235169149752;
	public static final double PIN_KING_O	=	4.400084487677882;
	public static final double PIN_KING_E	=	4.763515095856501;
	public static final double PIN_BIGGER_O	=	4.8010112003917484;
	public static final double PIN_BIGGER_E	=	6.4066615810464205;
	public static final double PIN_EQUAL_O	=	4.94761907883406;
	public static final double PIN_EQUAL_E	=	9.559151201492769;
	public static final double PIN_LOWER_O	=	0.7369572454951826;
	public static final double PIN_LOWER_E	=	3.0522002261614265;
	public static final double ATTACK_BIGGER_O	=	4.232728105857094;
	public static final double ATTACK_BIGGER_E	=	22.731304482994883;
	public static final double ATTACK_EQUAL_O	=	5.797031610592222;
	public static final double ATTACK_EQUAL_E	=	12.082858644052445;
	public static final double ATTACK_LOWER_O	=	2.2924847990005928;
	public static final double ATTACK_LOWER_E	=	6.533588111226228;
	public static final double HUNGED_PIECE_O	=	5.035823470670232;
	public static final double HUNGED_PIECE_E	=	10.387603531653903;
	public static final double HUNGED_PAWNS_O	=	0.8950906090115673;
	public static final double HUNGED_PAWNS_E	=	5.317972174452671;
	public static final double HUNGED_ALL_O	=	2.336728836626604;
	public static final double HUNGED_ALL_E	=	7.995315715674308;
}