package engine.entities.stats;

public class LevelStats extends Stats
{
	public LevelStats(MonsterRank rank, double baseValue, double min, double max)
	{
		super(baseValue, min, max);
		this.rank = rank;
	}

	public LevelStats(MonsterRank rank, double baseValue, double min)
	{
		this(rank, baseValue, min, Double.MAX_VALUE);
	}

	public LevelStats(MonsterRank rank, double baseValue)
	{
		this(rank, baseValue, -Double.MAX_VALUE, Double.MAX_VALUE);
	}

	public double calculateLinearLevelValue(int level, int maxLevel)
	{
		double constantOverLinear = ((rank.getMaxLevelConstant() / Math.sqrt(rank.getMaxLevelConstant())) * (maxLevel));
		// not exactly linear 'x' perfect function for its attenuation
		double value = Math.floor((rank.getMaxLevelConstant() / Math.sqrt(rank.getMaxLevelConstant())) * ((level) / (constantOverLinear / rank.getMaxLevelConstant())));
		return value;
	}

	public double calculateSqrtLevelValue(int level, int maxLevel)
	{
		double constantOverSqrt = (rank.getMaxLevelConstant() / Math.sqrt(rank.getMaxLevelConstant())) * Math.sqrt(maxLevel);
		double value = Math.floor((rank.getMaxLevelConstant() / Math.sqrt(rank.getMaxLevelConstant())) * (Math.sqrt(level) / (constantOverSqrt / rank.getMaxLevelConstant())));
		return value;
	}

	public double calculateSquareLevelValue(int level, int maxLevel)
	{
		double constantOverSquare = ((rank.getMaxLevelConstant() / Math.sqrt(rank.getMaxLevelConstant())) * (maxLevel * maxLevel));
		// not exactly square 'x²' function for its attenuation
		double value = Math.floor((rank.getMaxLevelConstant() / Math.sqrt(rank.getMaxLevelConstant())) * ((level * level) / (constantOverSquare / rank.getMaxLevelConstant())));
		return value;
	}
}
