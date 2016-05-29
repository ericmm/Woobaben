package woo.ba.ben.core;


import java.util.HashSet;
import java.util.Set;

public class ImmutableClasses {
    private static final Set<Class> IMMUTABLE_CLASS_SET = new HashSet<>(96);

    static {
        IMMUTABLE_CLASS_SET.add(java.awt.Font.class);
        IMMUTABLE_CLASS_SET.add(java.awt.BasicStroke.class);
        IMMUTABLE_CLASS_SET.add(java.awt.Color.class);
        IMMUTABLE_CLASS_SET.add(java.awt.GradientPaint.class);
        IMMUTABLE_CLASS_SET.add(java.awt.LinearGradientPaint.class);
        IMMUTABLE_CLASS_SET.add(java.awt.RadialGradientPaint.class);
        IMMUTABLE_CLASS_SET.add(java.awt.Cursor.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Boolean.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Byte.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Character.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Compiler.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Double.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Float.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Integer.class);
        IMMUTABLE_CLASS_SET.add(java.lang.invoke.MethodHandles.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Long.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Math.class);
        IMMUTABLE_CLASS_SET.add(java.lang.reflect.Array.class);
        IMMUTABLE_CLASS_SET.add(java.lang.reflect.ReflectPermission.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Runtime.class);
        IMMUTABLE_CLASS_SET.add(java.lang.RuntimePermission.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Short.class);
        IMMUTABLE_CLASS_SET.add(java.lang.StackTraceElement.class);
        IMMUTABLE_CLASS_SET.add(java.lang.StrictMath.class);
        IMMUTABLE_CLASS_SET.add(java.lang.String.class);
        IMMUTABLE_CLASS_SET.add(java.lang.System.class);
        IMMUTABLE_CLASS_SET.add(java.lang.Void.class);
        IMMUTABLE_CLASS_SET.add(java.math.BigInteger.class);
        IMMUTABLE_CLASS_SET.add(java.math.BigDecimal.class);
        IMMUTABLE_CLASS_SET.add(java.net.IDN.class);
        IMMUTABLE_CLASS_SET.add(java.net.NetPermission.class);
        IMMUTABLE_CLASS_SET.add(java.net.URL.class);
        IMMUTABLE_CLASS_SET.add(java.net.URI.class);
        IMMUTABLE_CLASS_SET.add(java.net.Inet4Address.class);
        IMMUTABLE_CLASS_SET.add(java.net.Inet6Address.class);
        IMMUTABLE_CLASS_SET.add(java.net.InetSocketAddress.class);
        IMMUTABLE_CLASS_SET.add(java.nio.channels.Channels.class);
        IMMUTABLE_CLASS_SET.add(java.nio.charset.CoderResult.class);
        IMMUTABLE_CLASS_SET.add(java.nio.charset.StandardCharsets.class);
        IMMUTABLE_CLASS_SET.add(java.io.File.class);
        IMMUTABLE_CLASS_SET.add(java.nio.file.Files.class);
        IMMUTABLE_CLASS_SET.add(java.nio.file.FileSystems.class);
        IMMUTABLE_CLASS_SET.add(java.nio.file.Paths.class);
        IMMUTABLE_CLASS_SET.add(java.rmi.server.RMIClassLoader.class);
        IMMUTABLE_CLASS_SET.add(java.rmi.server.UID.class);
        IMMUTABLE_CLASS_SET.add(java.security.AccessController.class);
        IMMUTABLE_CLASS_SET.add(java.security.AllPermission.class);
        IMMUTABLE_CLASS_SET.add(java.security.KeyStore.class);
        IMMUTABLE_CLASS_SET.add(java.security.ProtectionDomain.class);
        IMMUTABLE_CLASS_SET.add(java.security.Security.class);
        IMMUTABLE_CLASS_SET.add(java.security.SecurityPermission.class);
        IMMUTABLE_CLASS_SET.add(java.sql.DriverManager.class);
        IMMUTABLE_CLASS_SET.add(java.sql.SQLPermission.class);
        IMMUTABLE_CLASS_SET.add(java.sql.Types.class);
        IMMUTABLE_CLASS_SET.add(java.util.Arrays.class);
        IMMUTABLE_CLASS_SET.add(java.util.Collections.class);
        IMMUTABLE_CLASS_SET.add(java.util.Locale.class);
        IMMUTABLE_CLASS_SET.add(java.util.concurrent.Executors.class);
        IMMUTABLE_CLASS_SET.add(java.util.concurrent.locks.LockSupport.class);
        IMMUTABLE_CLASS_SET.add(java.util.concurrent.Semaphore.class);
        IMMUTABLE_CLASS_SET.add(java.util.concurrent.TimeUnit.class);
        IMMUTABLE_CLASS_SET.add(java.util.Currency.class);
        IMMUTABLE_CLASS_SET.add(java.util.Objects.class);
        IMMUTABLE_CLASS_SET.add(java.util.regex.Pattern.class);
        IMMUTABLE_CLASS_SET.add(java.util.TimeZone.class);
        IMMUTABLE_CLASS_SET.add(java.util.UUID.class);
        IMMUTABLE_CLASS_SET.add(java.util.zip.ZipFile.class);
        IMMUTABLE_CLASS_SET.add(java.time.LocalDate.class);
        IMMUTABLE_CLASS_SET.add(java.time.LocalTime.class);
        IMMUTABLE_CLASS_SET.add(java.time.LocalDateTime.class);
        IMMUTABLE_CLASS_SET.add(HeapObjectCopier.class);
        IMMUTABLE_CLASS_SET.add(UnsafeFactory.class);
        IMMUTABLE_CLASS_SET.add(ClassStructFactory.class);
    }

    private ImmutableClasses() {
    }

    public static boolean addImmutableClass(final Class clazz) {
        return IMMUTABLE_CLASS_SET.add(clazz);
    }

    public static boolean isImmutable(final Class clazz) {
        return IMMUTABLE_CLASS_SET.contains(clazz) || isAnnotationOrEnumOrInterface(clazz);
    }

    private static boolean isAnnotationOrEnumOrInterface(final Class clazz) {
        return clazz.isAnnotation() || clazz.isEnum() || clazz.isInterface();
    }
}
