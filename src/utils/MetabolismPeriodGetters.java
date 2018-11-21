package utils;

public abstract class MetabolismPeriodGetters {
	private MetabolismPeriodGetters() {
		throw new AssertionError("Uninstantiable");
	}
	
	public static MetabolismPeriodGetter constantMetabolismPeriodGetter(int period) {
		return new MetabolismPeriodGetter() {

			@Override
			public int metabolismPeriod() {
				return period;
			}

			@Override
			public void advanceStep() {}
			
		};
	}
	
	public static MetabolismPeriodGetter defaultStepwiseMetabolismPeriodGetter() {
		
		return new MetabolismPeriodGetter() {
			
			int step = 0;
			
			@Override
			public int metabolismPeriod() {
				if(step < 8) {
					return 1;
				} else if (step < 12) {
					return 2;
				} else if (step < 14) {
					return 4;
				}
				return 8;
			}

			@Override
			public void advanceStep() {
				step++;
			}
			
		};
	}
}
