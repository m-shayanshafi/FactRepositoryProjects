package bagaturchess.learning.impl.eval.cfg;


public interface Weights_NEW extends Weights_Manual {
	public static final double MATERIAL_PAWN_O	=	87.97298634995012;
	public static final double MATERIAL_PAWN_E	=	109.28318980655921;
	public static final double MATERIAL_KNIGHT_O	=	429.74430427269715;
	public static final double MATERIAL_KNIGHT_E	=	390.1940941358892;
	public static final double MATERIAL_BISHOP_O	=	446.4189099161886;
	public static final double MATERIAL_BISHOP_E	=	417.179939456182;
	public static final double MATERIAL_ROOK_O	=	615.3119329897646;
	public static final double MATERIAL_ROOK_E	=	678.5909064960134;
	public static final double MATERIAL_QUEEN_O	=	1351.3933882273832;
	public static final double MATERIAL_QUEEN_E	=	1238.3600417945097;
	public static final double KINGSAFE_CASTLING_O	=	8.289593963842078;
	public static final double KINGSAFE_CASTLING_E	=	0.0;
	//public static final double KINGSAFE_FIANCHETTO_O	=	0.0;
	//public static final double KINGSAFE_FIANCHETTO_E	=	0.0;
	public static final double BISHOPS_DOUBLE_O	=	38.81405542427409;
	public static final double BISHOPS_DOUBLE_E	=	59.37194485070076;
	public static final double KNIGHTS_DOUBLE_O	=	-0.7152164129948708;
	public static final double KNIGHTS_DOUBLE_E	=	-0.21805411991118545;
	public static final double ROOKS_DOUBLE_O	=	-0.5795680819005261;
	public static final double ROOKS_DOUBLE_E	=	-1.0660434231276967;
	public static final double PAWNS5_ROOKS_O	=	-0.5485692641871058;
	public static final double PAWNS5_ROOKS_E	=	-1.6544074707789533;
	public static final double PAWNS5_KNIGHTS_O	=	4.859476140422021;
	public static final double PAWNS5_KNIGHTS_E	=	8.064083080957985;
	public static final double KINGSAFE_F_O	=	-6.48498857261427;
	public static final double KINGSAFE_F_E	=	0.0;
	public static final double KINGSAFE_G_O	=	-0.013638664628148955;
	public static final double KINGSAFE_G_E	=	0.0;
	//public static final double KINGS_DISTANCE_O	=	2.516082293927031;
	//public static final double KINGS_DISTANCE_E	=	0.3342259482337531;
	public static final double PAWNS_DOUBLED_O	=	-0.05124911576743436;
	public static final double PAWNS_DOUBLED_E	=	-14.323207024787088;
	public static final double PAWNS_ISOLATED_O	=	-13.506657051418923;
	public static final double PAWNS_ISOLATED_E	=	-11.344258387244928;
	public static final double PAWNS_BACKWARD_O	=	-6.3682119596838165;
	public static final double PAWNS_BACKWARD_E	=	-2.23536621459676;
	public static final double PAWNS_SUPPORTED_O	=	3.890001103839263;
	public static final double PAWNS_SUPPORTED_E	=	3.210522079261474;
	public static final double PAWNS_CANNOTBS_O	=	-2.3200479636491735;
	public static final double PAWNS_CANNOTBS_E	=	-0.4574004927166371;
	public static final double PAWNS_PASSED_O	=	3.6854415952278554;
	public static final double PAWNS_PASSED_E	=	3.054235937517627;
	public static final double PAWNS_PASSED_RNK_O	=	1.1532030474618573;
	public static final double PAWNS_PASSED_RNK_E	=	1.5060582205826931;
	public static final double PAWNS_UNSTOPPABLE_PASSER_O	=	0.0;
	public static final double PAWNS_UNSTOPPABLE_PASSER_E	=	550.0;
	public static final double PAWNS_CANDIDATE_RNK_O	=	1.936000637671414;
	public static final double PAWNS_CANDIDATE_RNK_E	=	1.4805674847574313;
	public static final double KINGS_PASSERS_F_O	=	0.0;
	public static final double KINGS_PASSERS_F_E	=	1.8593443449388305;
	public static final double KINGS_PASSERS_FF_O	=	0.0;
	public static final double KINGS_PASSERS_FF_E	=	1.1208855282355108;
	public static final double KINGS_PASSERS_F_OP_O	=	0.0;
	public static final double KINGS_PASSERS_F_OP_E	=	1.6855334519533456;
	public static final double PAWNS_ISLANDS_O	=	-1.4032403667446314;
	public static final double PAWNS_ISLANDS_E	=	-0.6539450594678851;
	public static final double PAWNS_GARDS_O	=	18.45291809876759;
	public static final double PAWNS_GARDS_E	=	0.0;
	public static final double PAWNS_GARDS_REM_O	=	-7.8925521174123014;
	public static final double PAWNS_GARDS_REM_E	=	0.0;
	public static final double PAWNS_STORMS_O	=	3.91909437870527;
	public static final double PAWNS_STORMS_E	=	0.0;
	public static final double PAWNS_STORMS_CLS_O	=	2.072593633099025;
	public static final double PAWNS_STORMS_CLS_E	=	0.0;
	public static final double PAWNS_OPENNED_O	=	-35.70566231303562;
	public static final double PAWNS_OPENNED_E	=	0.0;
	public static final double PAWNS_SEMIOP_OWN_O	=	-25.68464357389989;
	public static final double PAWNS_SEMIOP_OWN_E	=	0.0;
	public static final double PAWNS_SEMIOP_OP_O	=	-12.063041264492604;
	public static final double PAWNS_SEMIOP_OP_E	=	0.0;
	public static final double PAWNS_WEAK_O	=	-1.5666981575451944;
	public static final double PAWNS_WEAK_E	=	-0.05059190918457653;
	public static final double SPACE_O	=	0.5535703987430469;
	public static final double SPACE_E	=	1.243482014276251;
	public static final double ROOK_INFRONT_PASSER_O	=	-25.406499653933455;
	public static final double ROOK_INFRONT_PASSER_E	=	-1.1346185206907324;
	//public static final double ROOK_BEHIND_PASSER_O	=	0.0826988829611304;
	//public static final double ROOK_BEHIND_PASSER_E	=	0.5415524194844257;
	public static final double PST_PAWN_O	=	0.5001508132219539;
	public static final double PST_PAWN_E	=	1.003880239393751;
	public static final double PST_KING_O	=	1.334137226669241;
	public static final double PST_KING_E	=	1.6992954614205182;
	public static final double PST_KNIGHTS_O	=	1.099635741114742;
	public static final double PST_KNIGHTS_E	=	1.1262979743191182;
	public static final double PST_BISHOPS_O	=	1.1741499091970307;
	public static final double PST_BISHOPS_E	=	1.440665398667129;
	public static final double PST_ROOKS_O	=	2.3057990961231276;
	public static final double PST_ROOKS_E	=	0.20829938789239588;
	public static final double PST_QUEENS_O	=	0.2994618382605751;
	public static final double PST_QUEENS_E	=	0.9398927229078035;
	public static final double BISHOPS_BAD_O	=	-0.4422282233930253;
	public static final double BISHOPS_BAD_E	=	-0.6780619833042797;
	public static final double KNIGHT_OUTPOST_O	=	6.534974327064092;
	public static final double KNIGHT_OUTPOST_E	=	0.9892275481160815;
	public static final double ROOKS_OPENED_O	=	27.610254974136286;
	public static final double ROOKS_OPENED_E	=	2.588294007008809;
	public static final double ROOKS_SEMIOPENED_O	=	19.665597125909066;
	public static final double ROOKS_SEMIOPENED_E	=	1.2464333914520824;
	public static final double TROPISM_KNIGHT_O	=	0.10721129415109834;
	public static final double TROPISM_KNIGHT_E	=	0.0;
	public static final double TROPISM_BISHOP_O	=	0.6251978429590173;
	public static final double TROPISM_BISHOP_E	=	0.0;
	public static final double TROPISM_ROOK_O	=	1.584392797872183;
	public static final double TROPISM_ROOK_E	=	0.0;
	public static final double TROPISM_QUEEN_O	=	0.45888912122635245;
	public static final double TROPISM_QUEEN_E	=	0.0;
	public static final double ROOKS_7TH_2TH_O	=	14.299164103308794;
	public static final double ROOKS_7TH_2TH_E	=	23.485771134727724;
	public static final double QUEENS_7TH_2TH_O	=	10.593060455648004;
	public static final double QUEENS_7TH_2TH_E	=	8.482310535545393;
	public static final double KINGSAFETY_L1_O	=	53.32695810035249;
	public static final double KINGSAFETY_L1_E	=	0.0;
	public static final double KINGSAFETY_L2_O	=	9.76038405503193;
	public static final double KINGSAFETY_L2_E	=	0.0;
	public static final double MOBILITY_KNIGHT_O	=	0.9000753633512448;
	public static final double MOBILITY_KNIGHT_E	=	1.4479247589042283;
	public static final double MOBILITY_BISHOP_O	=	1.1260706639961957;
	public static final double MOBILITY_BISHOP_E	=	1.207864155388296;
	public static final double MOBILITY_ROOK_O	=	0.903463571865006;
	public static final double MOBILITY_ROOK_E	=	1.3303929301705224;
	public static final double MOBILITY_QUEEN_O	=	0.12738625900925676;
	public static final double MOBILITY_QUEEN_E	=	0.8532060369360951;
	public static final double MOBILITY_KNIGHT_S_O	=	0.5601444500308435;
	public static final double MOBILITY_KNIGHT_S_E	=	1.0708139530408578;
	public static final double MOBILITY_BISHOP_S_O	=	0.6538417653757561;
	public static final double MOBILITY_BISHOP_S_E	=	0.18689674717438576;
	public static final double MOBILITY_ROOK_S_O	=	0.4258900572576672;
	public static final double MOBILITY_ROOK_S_E	=	1.1173027707751126;
	public static final double MOBILITY_QUEEN_S_O	=	0.10729890894706355;
	public static final double MOBILITY_QUEEN_S_E	=	0.43587109917528744;
	public static final double MOBILITY_S_POINTS_O	=	0.42307376374922284;
	public static final double MOBILITY_S_POINTS_E	=	0.3748640977791213;
	public static final double ROOKS_PAIR_H_O	=	8.394481727543303;
	public static final double ROOKS_PAIR_H_E	=	1.726818976920887;
	public static final double ROOKS_PAIR_V_O	=	0.08282088306641959;
	public static final double ROOKS_PAIR_V_E	=	0.4063630709592873;
	/*public static final double TRAP_KNIGHT_O	=	-0.420702378856173;
	public static final double TRAP_KNIGHT_E	=	-0.584092760963819;
	public static final double TRAP_BISHOP_O	=	-0.3984236297753586;
	public static final double TRAP_BISHOP_E	=	-0.634779381196584;
	public static final double TRAP_ROOK_O	=	-0.1661378091596628;
	public static final double TRAP_ROOK_E	=	-4.135319794625241;
	public static final double TRAP_QUEEN_O	=	0.0;
	public static final double TRAP_QUEEN_E	=	0.0;
	public static final double PIN_BK_O	=	19.39808966998984;
	public static final double PIN_BK_E	=	8.93308441963929;
	public static final double PIN_BQ_O	=	6.128170995233832;
	public static final double PIN_BQ_E	=	6.048200857637979;
	public static final double PIN_BR_O	=	5.889182491912925;
	public static final double PIN_BR_E	=	2.8057636381663387;
	public static final double PIN_BN_O	=	4.586721875762664;
	public static final double PIN_BN_E	=	9.645059089900874;
	public static final double PIN_RK_O	=	0.1584904001790113;
	public static final double PIN_RK_E	=	1.5643181072841534;
	public static final double PIN_RQ_O	=	3.4239091861888618;
	public static final double PIN_RQ_E	=	22.238677814133524;
	public static final double PIN_RB_O	=	0.17282224854604333;
	public static final double PIN_RB_E	=	3.7492890850451985;
	public static final double PIN_RN_O	=	0.17839946650663166;
	public static final double PIN_RN_E	=	3.406422113234788;
	public static final double PIN_QK_O	=	3.4220771588167653;
	public static final double PIN_QK_E	=	1.030421409144371;
	public static final double PIN_QQ_O	=	1.3185717130513932;
	public static final double PIN_QQ_E	=	14.170356800977917;
	public static final double PIN_QN_O	=	1.9400420912165284;
	public static final double PIN_QN_E	=	0.2449440662074017;
	public static final double PIN_QR_O	=	1.6088139858625663;
	public static final double PIN_QR_E	=	1.559887086071481;
	public static final double PIN_QB_O	=	0.14131398533885464;
	public static final double PIN_QB_E	=	1.0475041537149878;
	public static final double ATTACK_BN_O	=	5.5275200094383505;
	public static final double ATTACK_BN_E	=	17.928713990281718;
	public static final double ATTACK_BR_O	=	7.551701572948228;
	public static final double ATTACK_BR_E	=	35.52608425707156;
	public static final double ATTACK_NB_O	=	9.21212703579382;
	public static final double ATTACK_NB_E	=	8.894415850757778;
	public static final double ATTACK_NR_O	=	0.0715673558976888;
	public static final double ATTACK_NR_E	=	25.382982836454893;
	public static final double ATTACK_NQ_O	=	2.3478954455659125;
	public static final double ATTACK_NQ_E	=	34.810404312510634;
	public static final double ATTACK_RB_O	=	3.0180539918938294;
	public static final double ATTACK_RB_E	=	13.723377402893913;
	public static final double ATTACK_RN_O	=	5.523753637453827;
	public static final double ATTACK_RN_E	=	14.205105730293367;
	public static final double ATTACK_QN_O	=	1.374333557898814;
	public static final double ATTACK_QN_E	=	6.998730185335894;
	public static final double ATTACK_QB_O	=	1.7230947441063424;
	public static final double ATTACK_QB_E	=	1.9227999024761346;
	public static final double ATTACK_QR_O	=	5.71116184436602;
	public static final double ATTACK_QR_E	=	6.490312816470502;
	public static final double HUNGED_PIECE_1_O	=	-4.7653512139157375;
	public static final double HUNGED_PIECE_1_E	=	-12.25655888286756;
	public static final double HUNGED_PIECE_2_O	=	-9.475290683584197;
	public static final double HUNGED_PIECE_2_E	=	-22.20434688434407;
	public static final double HUNGED_PIECE_3_O	=	-7.559035243182996;
	public static final double HUNGED_PIECE_3_E	=	-28.106423678388715;
	public static final double HUNGED_PIECE_4_O	=	-13.04596537639506;
	public static final double HUNGED_PIECE_4_E	=	-12.018419311965095;
	public static final double HUNGED_PAWNS_1_O	=	-0.08113073269846297;
	public static final double HUNGED_PAWNS_1_E	=	-4.184217220211182;
	public static final double HUNGED_PAWNS_2_O	=	-0.24142228399768087;
	public static final double HUNGED_PAWNS_2_E	=	-6.772216144238311;
	public static final double HUNGED_PAWNS_3_O	=	-0.4328109762276074;
	public static final double HUNGED_PAWNS_3_E	=	-9.943444590000997;
	public static final double HUNGED_PAWNS_4_O	=	-0.44780688014599906;
	public static final double HUNGED_PAWNS_4_E	=	-21.785358381155703;
	public static final double HUNGED_ALL_1_O	=	-0.7209786036955272;
	public static final double HUNGED_ALL_1_E	=	-6.922058745129868;
	public static final double HUNGED_ALL_2_O	=	-1.2372479728747765;
	public static final double HUNGED_ALL_2_E	=	-10.58863319787238;
	public static final double HUNGED_ALL_3_O	=	-1.541189984889927;
	public static final double HUNGED_ALL_3_E	=	-14.130471947826923;
	public static final double HUNGED_ALL_4_O	=	-1.8583756831827187;
	public static final double HUNGED_ALL_4_E	=	-17.94963203723824;*/
}