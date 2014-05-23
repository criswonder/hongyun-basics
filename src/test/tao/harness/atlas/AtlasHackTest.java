package test.tao.harness.atlas;

import java.lang.reflect.Field;

import android.taobao.atlas.hack.Hack.HackDeclaration.HackAssertionException;
import android.taobao.atlas.hack.Interception;
import android.taobao.atlas.hack.Interception.InterceptionHandler;

public class AtlasHackTest {
	private static void fail(HackAssertionException e) throws HackAssertionException {
		throw new RuntimeException();
	}
	
	/** @beta */
	public static class HackedField<C, T> {

		public HackedField<C, T> ofGenericType(final Class<?> type) throws HackAssertionException {
			if (mField != null && !type.isAssignableFrom(mField.getType()))
				fail(new HackAssertionException(new ClassCastException(mField + " is not of type " + type)));
			return (HackedField<C, T>) this;
		}

		@SuppressWarnings("unchecked")
		public <T4> HackedField<C, T4> ofType(final Class<T4> type) throws HackAssertionException {
			if (mField != null && !type.isAssignableFrom(mField.getType()))
				fail(new HackAssertionException(new ClassCastException(mField + " is not of type " + type)));
			return (HackedField<C, T4>) this;
		}
		@SuppressWarnings("unchecked")
		public HackedField<C, T> ofType(final String type_name) throws HackAssertionException {
			try { return (HackedField<C, T>) ofType(Class.forName(type_name));
			} catch (final ClassNotFoundException e) { fail(new HackAssertionException(e)); return this; }
		}

		/** Get current value of this field */
		public T get() {
			try {
				@SuppressWarnings("unchecked") final T value = (T) mField.get(mObject);
			return value;
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
				//TBS.Ext.commitEvent("AtlasRuntimeException", AtlasConstant.ATLAS_RUNTIME_EXCEPTION, e.toString());
				return null; /* Should never happen */ }
		}

		/**
		 * Set value of this field
		 * 
		 * <p>No type enforced here since most type mismatch can be easily tested and exposed early.</p>
		 */
		public void set(final Object value) {
			try {
			mField.set(mObject, value);
			} catch (final IllegalAccessException e) { 
				e.printStackTrace();
				/* Should never happen */
			}
		}

		/**
		 * Hijack the current instance of this field.
		 *
		 * <p><b>The instance must not be null at the time of hijacking</b>, or an IllegalStateException will be thrown.
		 *
		 * @param handler a invocation handler to implement the hijack logic.
		 */
		public void hijack(final InterceptionHandler<?> handler) {
			final Object delegatee = get();
			if (delegatee == null) throw new IllegalStateException("Cannot hijack null");
			final Class<?>[] interfaces = delegatee.getClass().getInterfaces();
			set(Interception.proxy(delegatee, handler, interfaces));
		}

		public HackedField<C, T> on(final C instance) {
			mObject = instance;
			return this;
		}

		/** @param modifiers the modifiers this field must have */
		HackedField(final Class<C> clazz, final String name, int modifiers) throws HackAssertionException {
			Field field = null;
			try {
				if (clazz == null) return;
				mObject = null;
				field = clazz.getDeclaredField(name);
				if (modifiers > 0 && (field.getModifiers() & modifiers) != modifiers)
					fail(new HackAssertionException(field + " does not match modifiers: " + modifiers));
				field.setAccessible(true);
			} catch (final NoSuchFieldException e) {
				HackAssertionException hae = new HackAssertionException(e);
				hae.setHackedClass(clazz);
				hae.setHackedFieldName(name);
				fail(hae);
			} finally { mField = field; }
		}

		private Object mObject;
		private final Field mField;
	}

}
